package com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository

import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineDao
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import kotlinx.coroutines.flow.Flow

class VaccineRepository(
    private val vaccineDao: VaccineDao,
) {

    fun getVaccinesByPetId(
        petId: Long,
    ): Flow<List<VaccineEntity>> {
        return vaccineDao.getVaccinesByPetId(petId)
    }

    fun getVaccineById(
        vaccineId: Long,
    ): Flow<VaccineEntity?> {
        return vaccineDao.getVaccineById(vaccineId)
    }

    suspend fun insertVaccine(
        vaccine: VaccineEntity,
    ): Long {
        return vaccineDao.insertVaccine(vaccine)
    }

    suspend fun updateVaccine(
        vaccine: VaccineEntity,
    ) {
        vaccineDao.updateVaccine(vaccine)
    }

    suspend fun deleteVaccine(
        vaccine: VaccineEntity,
    ) {
        vaccineDao.deleteVaccine(vaccine)
    }
}