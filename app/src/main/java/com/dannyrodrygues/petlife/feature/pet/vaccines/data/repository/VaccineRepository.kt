package com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository

import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineDao
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import kotlinx.coroutines.flow.Flow

class VaccineRepository(
    private val vaccineDao: VaccineDao,
    private val tenantId: String,
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
        val tenantVaccine = vaccine.copy(
            tenantId = tenantId,
        )

        return vaccineDao.insertVaccine(
            tenantVaccine,
        )
    }

    suspend fun updateVaccine(
        vaccine: VaccineEntity,
    ) {
        require(vaccine.tenantId == tenantId) {
            "Não é permitido alterar uma vacina de outro Tenant."
        }

        vaccineDao.updateVaccine(vaccine)
    }

    suspend fun deleteVaccine(
        vaccine: VaccineEntity,
    ) {
        require(vaccine.tenantId == tenantId) {
            "Não é permitido excluir uma vacina de outro Tenant."
        }

        vaccineDao.deleteVaccine(vaccine)
    }
}