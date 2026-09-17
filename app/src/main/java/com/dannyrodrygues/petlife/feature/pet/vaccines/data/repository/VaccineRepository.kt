package com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository

import com.dannyrodrygues.petlife.feature.pet.data.local.PetDao
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineDao
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.mapper.toEntity
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.mapper.toInsertDto
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.mapper.toUpdateDto
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.remote.VaccineRemoteDataSource
import kotlinx.coroutines.flow.Flow

class VaccineRepository(
    private val vaccineDao: VaccineDao,
    private val petDao: PetDao,
    private val tenantId: String,
    private val remoteDataSource: VaccineRemoteDataSource =
        VaccineRemoteDataSource(),
) {

    init {
        require(tenantId.isNotBlank()) {
            "tenantId não pode estar vazio."
        }
    }

    fun getVaccinesByPetId(
        petId: Long,
    ): Flow<List<VaccineEntity>> {
        return vaccineDao.getVaccinesByPetId(
            petId = petId,
            tenantId = tenantId,
        )
    }

    fun getVaccineById(
        vaccineId: Long,
    ): Flow<VaccineEntity?> {
        return vaccineDao.getVaccineById(
            vaccineId = vaccineId,
            tenantId = tenantId,
        )
    }

    suspend fun insertVaccine(
        vaccine: VaccineEntity,
    ): Long {

        /*
         * Garante que a Vacina pertence
         * ao Tenant autenticado.
         */
        val tenantVaccine = vaccine.copy(
            tenantId = tenantId,
        )

        /*
         * Primeiro salva localmente.
         */
        val localVaccineId = vaccineDao.insertVaccine(
            tenantVaccine,
        )

        /*
         * Busca o Pet relacionado no Room.
         */
        val pet = petDao.getPetSnapshotById(
            petId = tenantVaccine.petId,
            tenantId = tenantId,
        )

        /*
         * Para criar a Vacina no Supabase,
         * o Pet precisa já possuir UUID remoto.
         *
         * Se o Pet ainda estiver pendente de sync,
         * mantemos a Vacina somente no Room.
         */
        val remotePetId = pet?.remoteId
            ?: return localVaccineId

        /*
         * Tenta criar a Vacina remotamente.
         *
         * Falha de conexão não remove
         * o registro local.
         */
        runCatching {
            val remoteVaccine =
                remoteDataSource.insertVaccine(
                    tenantVaccine.toInsertDto(
                        remotePetId = remotePetId,
                    ),
                )

            /*
             * Guarda o UUID retornado
             * pelo Supabase no Room.
             */
            vaccineDao.updateRemoteId(
                localVaccineId = localVaccineId,
                tenantId = tenantId,
                remoteId = remoteVaccine.id,
            )
        }

        return localVaccineId
    }

    suspend fun updateVaccine(
        vaccine: VaccineEntity,
    ) {
        require(vaccine.tenantId == tenantId) {
            "Não é permitido alterar uma vacina de outro Tenant."
        }

        /*
         * Primeiro atualiza o Room.
         */
        vaccineDao.updateVaccine(vaccine)

        /*
         * Se a Vacina ainda não existe no Supabase,
         * não há UPDATE remoto para fazer.
         *
         * Na próxima sincronização ela será enviada
         * como uma nova Vacina com os dados atualizados.
         */
        val remoteId = vaccine.remoteId
            ?: return

        /*
         * Marca que existe uma alteração local
         * aguardando sincronização.
         */
        vaccineDao.markPendingSync(
            vaccineId = vaccine.id,
            tenantId = tenantId,
        )

        /*
         * Tenta atualizar imediatamente no Supabase.
         */
        runCatching {
            remoteDataSource.updateVaccine(
                vaccineId = remoteId,
                tenantId = tenantId,
                vaccine = vaccine.toUpdateDto(),
            )

            /*
             * Se deu certo, limpa a pendência.
             */
            vaccineDao.markSynced(
                vaccineId = vaccine.id,
                tenantId = tenantId,
            )
        }

        /*
         * Se falhar por falta de internet,
         * pendingSync continua = 1.
         */
    }

    suspend fun deleteVaccine(
        vaccine: VaccineEntity,
    ) {
        require(vaccine.tenantId == tenantId) {
            "Não é permitido excluir uma vacina de outro Tenant."
        }

        val remoteId = vaccine.remoteId

        /*
         * A vacina ainda existe somente no Room.
         * Nesse caso podemos apagar fisicamente.
         */
        if (remoteId == null) {
            vaccineDao.deleteVaccine(vaccine)
            return
        }

        /*
         * A vacina já existe no Supabase.
         *
         * Primeiro escondemos localmente para que ela
         * desapareça da tela imediatamente.
         */
        vaccineDao.markPendingDelete(
            vaccineId = vaccine.id,
            tenantId = tenantId,
        )

        /*
         * Depois tentamos o soft delete no Supabase.
         */
        val deletedRemotely = runCatching {
            remoteDataSource.softDeleteVaccine(
                vaccineId = remoteId,
                tenantId = tenantId,
            )
        }.isSuccess

        /*
         * Se o Supabase confirmou a exclusão,
         * podemos remover fisicamente do Room.
         *
         * Se não havia internet, ela continua no Room
         * com pendingDelete = 1 e permanece escondida.
         */
        if (deletedRemotely) {
            vaccineDao.deleteVaccine(vaccine)
        }
    }

    suspend fun syncUnsyncedVaccinesToRemote() {
        val unsyncedVaccines =
            vaccineDao.getUnsyncedVaccines(
                tenantId = tenantId,
            )

        unsyncedVaccines.forEach { localVaccine ->

            /*
             * Localiza o Pet ao qual
             * esta Vacina pertence.
             */
            val pet = petDao.getPetSnapshotById(
                petId = localVaccine.petId,
                tenantId = tenantId,
            )

            /*
             * A Vacina só pode ser enviada
             * depois que o Pet possuir
             * um UUID remoto.
             */
            val remotePetId = pet?.remoteId
                ?: return@forEach

            /*
             * Cada Vacina é sincronizada
             * individualmente.
             *
             * Se uma falhar, as demais ainda
             * podem continuar sendo processadas.
             */
            runCatching {
                val remoteVaccine =
                    remoteDataSource.insertVaccine(
                        localVaccine.toInsertDto(
                            remotePetId = remotePetId,
                        ),
                    )

                /*
                 * Guarda no Room o UUID
                 * criado pelo Supabase.
                 */
                vaccineDao.updateRemoteId(
                    localVaccineId = localVaccine.id,
                    tenantId = tenantId,
                    remoteId = remoteVaccine.id,
                )
            }
        }
    }

    suspend fun syncPendingVaccineDeletes() {
        val pendingDeletes =
            vaccineDao.getPendingDeleteVaccines(
                tenantId = tenantId,
            )

        pendingDeletes.forEach { localVaccine ->

            val remoteId = localVaccine.remoteId

            /*
             * Segurança extra:
             * se por algum motivo não houver remoteId,
             * basta remover localmente.
             */
            if (remoteId == null) {
                vaccineDao.deleteVaccine(localVaccine)
                return@forEach
            }

            val deletedRemotely = runCatching {
                remoteDataSource.softDeleteVaccine(
                    vaccineId = remoteId,
                    tenantId = tenantId,
                )
            }.isSuccess

            /*
             * Só removemos fisicamente do Room
             * depois que o Supabase confirmar.
             */
            if (deletedRemotely) {
                vaccineDao.deleteVaccine(localVaccine)
            }
        }
    }

    suspend fun syncRemoteVaccinesToLocal() {
        runCatching {

            val remoteVaccines =
                remoteDataSource.getVaccinesByTenantId(
                    tenantId = tenantId,
                )

            remoteVaccines.forEach { remoteVaccine ->
                /*
                 * Vacina excluída remotamente.
                 */
                if (remoteVaccine.deletedAt != null) {

                    val existingVaccine =
                        vaccineDao.getVaccineByRemoteId(
                            remoteId = remoteVaccine.id,
                            tenantId = tenantId,
                        )

                    if (existingVaccine != null) {
                        vaccineDao.deleteVaccine(
                            existingVaccine,
                        )
                    }

                    return@forEach
                }

                /*
                 * Procura a vacina no Room.
                 */
                val existingVaccine =
                    vaccineDao.getVaccineByRemoteId(
                        remoteId = remoteVaccine.id,
                        tenantId = tenantId,
                    )

                /*
                 * Ainda não existe localmente.
                 */
                if (existingVaccine == null) {

                    val localPet =
                        petDao.getPetByRemoteId(
                            remoteId = remoteVaccine.petId,
                            tenantId = tenantId,
                        )

                    if (localPet != null) {
                        vaccineDao.insertVaccine(
                            remoteVaccine.toEntity(
                                localPetId = localPet.id,
                            ),
                        )
                    }

                    return@forEach
                }

                /*
                 * Existe localmente, mas há uma alteração
                 * ou exclusão aguardando sincronização.
                 *
                 * Nesse caso, preservamos o estado local.
                 */
                if (
                    existingVaccine.pendingSync ||
                    existingVaccine.pendingDelete
                ) {
                    return@forEach
                }

                /*
                 * Sem pendência local:
                 * aceita os dados do Supabase.
                 */
                val remoteEntity =
                    remoteVaccine.toEntity(
                        localPetId = existingVaccine.petId,
                    )

                vaccineDao.updateVaccine(
                    remoteEntity.copy(
                        id = existingVaccine.id,
                        petId = existingVaccine.petId,
                    ),
                )
            }
        }
    }

    suspend fun syncPendingVaccineUpdates() {
        val pendingVaccines =
            vaccineDao.getPendingSyncVaccines(
                tenantId = tenantId,
            )

        pendingVaccines.forEach { localVaccine ->

            val remoteId =
                localVaccine.remoteId
                    ?: return@forEach

            runCatching {
                remoteDataSource.updateVaccine(
                    vaccineId = remoteId,
                    tenantId = tenantId,
                    vaccine = localVaccine.toUpdateDto(),
                )

                vaccineDao.markSynced(
                    vaccineId = localVaccine.id,
                    tenantId = tenantId,
                )
            }
        }
    }
}