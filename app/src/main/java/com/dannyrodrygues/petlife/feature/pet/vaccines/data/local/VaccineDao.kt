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
          AND tenantId = :tenantId
          AND pendingDelete = 0
        ORDER BY applicationDateMillis DESC
        """
    )
    fun getVaccinesByPetId(
        petId: Long,
        tenantId: String,
    ): Flow<List<VaccineEntity>>

    @Query(
        """
        SELECT * FROM vaccines
        WHERE id = :vaccineId
          AND tenantId = :tenantId
          AND pendingDelete = 0
        LIMIT 1
        """
    )
    fun getVaccineById(
        vaccineId: Long,
        tenantId: String,
    ): Flow<VaccineEntity?>

    /*
     * Relaciona a Vacina local ao UUID
     * criado pelo Supabase.
     */
    @Query(
        """
        UPDATE vaccines
        SET remoteId = :remoteId
        WHERE id = :localVaccineId
          AND tenantId = :tenantId
        """
    )
    suspend fun updateRemoteId(
        localVaccineId: Long,
        tenantId: String,
        remoteId: String,
    )

    /*
     * Localiza uma Vacina pelo UUID remoto.
     * Utilizado para evitar duplicações.
     */
    @Query(
        """
        SELECT * FROM vaccines
        WHERE remoteId = :remoteId
          AND tenantId = :tenantId
        LIMIT 1
        """
    )
    suspend fun getVaccineByRemoteId(
        remoteId: String,
        tenantId: String,
    ): VaccineEntity?

    /*
     * Vacinas criadas localmente que ainda
     * nunca foram enviadas ao Supabase.
     */
    @Query(
        """
        SELECT * FROM vaccines
        WHERE tenantId = :tenantId
          AND remoteId IS NULL
          AND pendingDelete = 0
        ORDER BY id ASC
        """
    )
    suspend fun getUnsyncedVaccines(
        tenantId: String,
    ): List<VaccineEntity>

    /*
     * Vacinas já existentes remotamente,
     * mas que possuem edição local pendente.
     */
    @Query(
        """
        SELECT * FROM vaccines
        WHERE tenantId = :tenantId
          AND pendingSync = 1
          AND remoteId IS NOT NULL
          AND pendingDelete = 0
        ORDER BY id ASC
        """
    )
    suspend fun getPendingSyncVaccines(
        tenantId: String,
    ): List<VaccineEntity>

    /*
     * Vacinas aguardando exclusão remota.
     */
    @Query(
        """
        SELECT * FROM vaccines
        WHERE tenantId = :tenantId
          AND pendingDelete = 1
        ORDER BY id ASC
        """
    )
    suspend fun getPendingDeleteVaccines(
        tenantId: String,
    ): List<VaccineEntity>

    @Query(
        """
        UPDATE vaccines
        SET pendingSync = 1
        WHERE id = :vaccineId
          AND tenantId = :tenantId
        """
    )
    suspend fun markPendingSync(
        vaccineId: Long,
        tenantId: String,
    )

    @Query(
        """
        UPDATE vaccines
        SET pendingSync = 0
        WHERE id = :vaccineId
          AND tenantId = :tenantId
        """
    )
    suspend fun markSynced(
        vaccineId: Long,
        tenantId: String,
    )

    @Query(
        """
        UPDATE vaccines
        SET pendingDelete = 1
        WHERE id = :vaccineId
          AND tenantId = :tenantId
        """
    )
    suspend fun markPendingDelete(
        vaccineId: Long,
        tenantId: String,
    )

    @Update
    suspend fun updateVaccine(
        vaccine: VaccineEntity,
    )

    @Delete
    suspend fun deleteVaccine(
        vaccine: VaccineEntity,
    )
}