package com.dannyrodrygues.petlife.feature.pet.data.repository

import com.dannyrodrygues.petlife.feature.pet.data.local.PetDao
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.mapper.toEntity
import com.dannyrodrygues.petlife.feature.pet.data.mapper.toInsertDto
import com.dannyrodrygues.petlife.feature.pet.data.mapper.toUpdateDto
import com.dannyrodrygues.petlife.feature.pet.data.remote.PetRemoteDataSource
import kotlinx.coroutines.flow.Flow

class PetRepository(
    private val petDao: PetDao,
    private val tenantId: String,
    private val remoteDataSource: PetRemoteDataSource =
        PetRemoteDataSource(),
) {

    init {
        require(tenantId.isNotBlank()) {
            "tenantId não pode estar vazio."
        }
    }
    fun getAllPets(): Flow<List<PetEntity>> {
        return petDao.getAllPets(
            tenantId = tenantId,
        )
    }

    fun getPetById(
        petId: Long,
    ): Flow<PetEntity?> {
        return petDao.getPetById(
            petId = petId,
            tenantId = tenantId,
        )
    }

    suspend fun insertPet(
        pet: PetEntity,
    ): Long {

        /*
         * Garante que o Pet sempre pertence
         * ao Tenant autenticado.
         */
        val tenantPet = pet.copy(
            tenantId = tenantId,
        )

        /*
         * Primeiro salva localmente.
         */
        val localPetId = petDao.insertPet(
            tenantPet,
        )

        /*
         * Depois tenta criar o registro remoto.
         *
         * Se estiver offline ou o Supabase falhar,
         * o Pet local continua preservado.
         */
        runCatching {
            val remotePet = remoteDataSource.insertPet(
                tenantPet.toInsertDto(),
            )

            /*
             * Relaciona o ID local ao UUID
             * gerado pelo Supabase.
             */
            petDao.updateRemoteId(
                localPetId = localPetId,
                tenantId = tenantId,
                remoteId = remotePet.id,
            )
        }

        return localPetId
    }

    suspend fun updatePet(
        pet: PetEntity,
    ) {
        require(pet.tenantId == tenantId) {
            "Não é permitido alterar um Pet de outro Tenant."
        }

        /*
         * Salva os novos dados localmente.
         */
        val localPet = pet.copy(
            tenantId = tenantId,
        )

        petDao.updatePet(localPet)

        /*
         * Marca explicitamente que existe
         * uma alteração aguardando sincronização.
         */
        petDao.markPendingSync(
            petId = localPet.id,
            tenantId = tenantId,
        )

        /*
         * Se ainda não existe no Supabase,
         * permanece pendente.
         */
        val remoteId = localPet.remoteId
            ?: return

        /*
         * Tenta atualizar o registro remoto.
         */
        val remoteUpdateSucceeded =
            runCatching {
                remoteDataSource.updatePet(
                    remoteId = remoteId,
                    tenantId = tenantId,
                    pet = localPet.toUpdateDto(),
                )
            }.isSuccess

        /*
         * Só limpa a pendência se o Supabase
         * confirmou a atualização.
         */
        if (remoteUpdateSucceeded) {
            petDao.markSynced(
                petId = localPet.id,
                tenantId = tenantId,
            )
        }
    }


    suspend fun deletePet(
        pet: PetEntity,
    ) {
        require(pet.tenantId == tenantId) {
            "Não é permitido excluir um Pet de outro Tenant."
        }

        /*
         * Se o Pet nunca foi enviado ao Supabase,
         * podemos removê-lo definitivamente do Room.
         */
        val remoteId = pet.remoteId

        if (remoteId == null) {
            petDao.deletePet(pet)
            return
        }

        /*
         * O Pet já existe remotamente.
         *
         * Primeiro marcamos a exclusão local como pendente.
         * Como getAllPets() filtra pendingDelete = 0,
         * ele desaparece imediatamente da interface.
         */
        petDao.markPendingDelete(
            petId = pet.id,
            tenantId = tenantId,
        )

        /*
         * Tenta realizar o soft delete no Supabase.
         */
        val remoteDeleteSucceeded =
            runCatching {
                remoteDataSource.softDeletePet(
                    remoteId = remoteId,
                    tenantId = tenantId,
                )
            }.isSuccess

        /*
         * Só removemos definitivamente do Room
         * depois que o Supabase confirmou a exclusão.
         *
         * Se estiver offline, a linha permanece no Room
         * com pendingDelete = 1 para sincronização futura.
         */
        if (remoteDeleteSucceeded) {
            petDao.deletePet(pet)
        }
    }

    suspend fun syncRemotePetsToLocal() {
        runCatching {
            val remotePets = remoteDataSource
                .getPetsByTenantId(
                    tenantId = tenantId,
                )

            /*
             * Primeiro processa exclusões remotas.
             */
            remotePets
                .filter { remotePet ->
                    remotePet.deletedAt != null
                }
                .forEach { remotePet ->

                    val localPet = petDao.getPetByRemoteId(
                        remoteId = remotePet.id,
                        tenantId = tenantId,
                    )

                    if (localPet != null) {
                        petDao.deletePet(localPet)
                    }
                }

            /*
             * Depois processa Pets ativos.
             */
            remotePets
                .filter { remotePet ->
                    remotePet.deletedAt == null
                }
                .forEach { remotePet ->

                    val existingPet = petDao.getPetByRemoteId(
                        remoteId = remotePet.id,
                        tenantId = tenantId,
                    )

                    if (existingPet == null) {

                        /*
                         * Ainda não existe localmente.
                         */
                        petDao.insertPet(
                            remotePet.toEntity(),
                        )

                    } else if (
                        !existingPet.pendingSync &&
                        !existingPet.pendingDelete
                    ) {

                        /*
                         * Já existe no Room e não possui
                         * alteração local pendente.
                         *
                         * Nesse caso, os dados remotos
                         * podem atualizar o registro local.
                         */
                        val updatedLocalPet =
                            remotePet.toEntity().copy(
                                id = existingPet.id,

                                /*
                                 * Foto local ainda não está
                                 * sincronizada com Storage.
                                 */
                                photoUri = existingPet.photoUri,

                                pendingSync = false,
                                pendingDelete = false,
                            )

                        petDao.updatePet(
                            updatedLocalPet,
                        )
                    }
                }
        }
    }

    suspend fun syncUnsyncedPetsToRemote() {
        val unsyncedPets = petDao.getUnsyncedPets(
            tenantId = tenantId,
        )

        unsyncedPets.forEach { localPet ->

            /*
             * Cada Pet é sincronizado individualmente.
             *
             * Se um deles falhar por falta de internet,
             * os demais ainda podem ser processados.
             */
            runCatching {
                val remotePet = remoteDataSource.insertPet(
                    localPet.toInsertDto(),
                )

                /*
                 * Guarda no Room o UUID criado
                 * pelo Supabase.
                 */
                petDao.updateRemoteId(
                    localPetId = localPet.id,
                    tenantId = tenantId,
                    remoteId = remotePet.id,
                )
            }
        }
    }

    suspend fun syncPendingPetUpdates() {
        val pendingPets = petDao.getPendingSyncPets(
            tenantId = tenantId,
        )

        pendingPets.forEach { localPet ->

            val remoteId = localPet.remoteId
                ?: return@forEach

            val updateSucceeded =
                runCatching {
                    remoteDataSource.updatePet(
                        remoteId = remoteId,
                        tenantId = tenantId,
                        pet = localPet.toUpdateDto(),
                    )
                }.isSuccess

            if (updateSucceeded) {
                petDao.updatePet(
                    localPet.copy(
                        pendingSync = false,
                    ),
                )
            }
        }
    }

    suspend fun syncPendingPetDeletes() {
        val pendingDeletes = petDao.getPendingDeletePets(
            tenantId = tenantId,
        )

        pendingDeletes.forEach { localPet ->

            val remoteId = localPet.remoteId

            /*
             * Se por algum motivo o Pet não tiver remoteId,
             * não existe nada remoto para excluir.
             */
            if (remoteId == null) {
                petDao.deletePet(localPet)
                return@forEach
            }

            val remoteDeleteSucceeded =
                runCatching {
                    remoteDataSource.softDeletePet(
                        remoteId = remoteId,
                        tenantId = tenantId,
                    )
                }.isSuccess

            /*
             * Só removemos do Room quando
             * o Supabase confirmar o soft delete.
             */
            if (remoteDeleteSucceeded) {
                petDao.deletePet(localPet)
            }
        }
    }
}