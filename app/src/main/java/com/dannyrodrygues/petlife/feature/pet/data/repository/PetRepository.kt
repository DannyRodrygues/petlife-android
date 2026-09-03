package com.dannyrodrygues.petlife.feature.pet.data.repository

import com.dannyrodrygues.petlife.feature.pet.data.local.PetDao
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(
    private val petDao: PetDao,
    private val tenantId: String,
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
        val tenantPet = pet.copy(
            tenantId = tenantId,
        )

        return petDao.insertPet(tenantPet)
    }

    suspend fun updatePet(
        pet: PetEntity,
    ) {
        require(pet.tenantId == tenantId) {
            "Não é permitido alterar um Pet de outro Tenant."
        }

        petDao.updatePet(pet)
    }

    suspend fun deletePet(
        pet: PetEntity,
    ) {
        require(pet.tenantId == tenantId) {
            "Não é permitido excluir um Pet de outro Tenant."
        }

        petDao.deletePet(pet)
    }
}