package com.dannyrodrygues.petlife.feature.pet.vaccines.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVaccine(
        vaccine: VaccineEntity,
    ): Long

    @Query(
        """
        SELECT * FROM vaccines
        WHERE petId = :petId
        ORDER BY applicationDateMillis DESC
        """
    )
    fun getVaccinesByPetId(
        petId: Long,
    ): Flow<List<VaccineEntity>>

    @Query(
        """
        SELECT * FROM vaccines
        WHERE id = :vaccineId
        LIMIT 1
        """
    )
    fun getVaccineById(
        vaccineId: Long,
    ): Flow<VaccineEntity?>

    @Update
    suspend fun updateVaccine(
        vaccine: VaccineEntity,
    )

    @Delete
    suspend fun deleteVaccine(
        vaccine: VaccineEntity,
    )
}