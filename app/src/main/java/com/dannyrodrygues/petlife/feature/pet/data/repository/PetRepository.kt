package com.dannyrodrygues.petlife.feature.pet.data.repository

import com.dannyrodrygues.petlife.feature.pet.data.local.PetDao
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import kotlinx.coroutines.flow.Flow

class PetRepository(
    private val petDao: PetDao,
) {

    fun getAllPets(): Flow<List<PetEntity>> {
        return petDao.getAllPets()
    }

    fun getPetById(petId: Long): Flow<PetEntity?> {
        return petDao.getPetById(petId)
    }

    suspend fun insertPet(pet: PetEntity): Long {
        return petDao.insertPet(pet)
    }

    suspend fun updatePet(pet: PetEntity) {
        petDao.updatePet(pet)
    }

    suspend fun deletePet(pet: PetEntity) {
        petDao.deletePet(pet)
    }
}

