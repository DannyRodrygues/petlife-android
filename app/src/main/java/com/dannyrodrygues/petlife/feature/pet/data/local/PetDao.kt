package com.dannyrodrygues.petlife.feature.pet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity): Long

    @Query(
        """
        SELECT * FROM pets
        WHERE tenantId = :tenantId
        ORDER BY name ASC
        """,
    )
    fun getAllPets(
        tenantId: String,
    ): Flow<List<PetEntity>>

    @Query(
        """
        SELECT * FROM pets
        WHERE id = :petId
          AND tenantId = :tenantId
        LIMIT 1
        """,
    )
    fun getPetById(
        petId: Long,
        tenantId: String,
    ): Flow<PetEntity?>

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)
}