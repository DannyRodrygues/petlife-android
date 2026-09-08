package com.dannyrodrygues.petlife.feature.pet.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity): Long

    @Serializable
    data class PetDeleteDto(
        @SerialName("deleted_at")
        val deletedAt: String,
    )

    @Query(
        """
    SELECT * FROM pets
    WHERE tenantId = :tenantId
      AND pendingDelete = 0
    ORDER BY id DESC
    """
    )
    fun getAllPets(
        tenantId: String,
    ): Flow<List<PetEntity>>

    @Query(
        """
    UPDATE pets
    SET remoteId = :remoteId
    WHERE id = :localPetId
      AND tenantId = :tenantId
    """
    )
    suspend fun updateRemoteId(
        localPetId: Long,
        tenantId: String,
        remoteId: String,
    )

    @Query(
        """
    SELECT * FROM pets
    WHERE id = :petId
      AND tenantId = :tenantId
      AND pendingDelete = 0
    LIMIT 1
    """
    )
    fun getPetById(
        petId: Long,
        tenantId: String,
    ): Flow<PetEntity?>

    @Query(
        """
    SELECT * FROM pets
    WHERE remoteId = :remoteId
      AND tenantId = :tenantId
    LIMIT 1
    """
    )
    suspend fun getPetByRemoteId(
        remoteId: String,
        tenantId: String,
    ): PetEntity?

    @Query(
        """
    SELECT * FROM pets
    WHERE tenantId = :tenantId
      AND remoteId IS NULL
    ORDER BY id ASC
    """
    )
    suspend fun getUnsyncedPets(
        tenantId: String,
    ): List<PetEntity>

    @Query(
        """
    SELECT * FROM pets
    WHERE tenantId = :tenantId
      AND pendingSync = 1
      AND remoteId IS NOT NULL
    ORDER BY id ASC
    """
    )
    suspend fun getPendingSyncPets(
        tenantId: String,
    ): List<PetEntity>

    @Query(
        """
    UPDATE pets
    SET pendingSync = 1
    WHERE id = :petId
      AND tenantId = :tenantId
    """
    )
    suspend fun markPendingSync(
        petId: Long,
        tenantId: String,
    )

    @Query(
        """
    UPDATE pets
    SET pendingSync = 0
    WHERE id = :petId
      AND tenantId = :tenantId
    """
    )
    suspend fun markSynced(
        petId: Long,
        tenantId: String,
    )

    @Query(
        """
    UPDATE pets
    SET pendingDelete = 1
    WHERE id = :petId
      AND tenantId = :tenantId
    """
    )
    suspend fun markPendingDelete(
        petId: Long,
        tenantId: String,
    )

    @Query(
        """
    SELECT * FROM pets
    WHERE tenantId = :tenantId
      AND pendingDelete = 1
    ORDER BY id ASC
    """
    )
    suspend fun getPendingDeletePets(
        tenantId: String,
    ): List<PetEntity>

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)
}