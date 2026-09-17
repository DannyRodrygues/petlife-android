package com.dannyrodrygues.petlife.feature.pet.vaccines.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity

@Entity(
    tableName = "vaccines",
    foreignKeys = [
        ForeignKey(
            entity = PetEntity::class,
            parentColumns = ["id"],
            childColumns = ["petId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["petId"]),
        Index(value = ["tenantId", "petId"]),
        Index(value = ["remoteId"], unique = true),
    ],
)
data class VaccineEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val tenantId: String = "",

    /*
     * ID local do Pet no Room.
     */
    val petId: Long,

    /*
     * UUID da Vacina correspondente no Supabase.
     */
    val remoteId: String? = null,

    val name: String,

    val doseDescription: String?,

    val applicationDateMillis: Long?,

    val nextDoseDateMillis: Long?,

    val observations: String?,

    /*
     * Indica uma edição ainda não sincronizada.
     */
    val pendingSync: Boolean = false,

    /*
     * Indica uma exclusão ainda não sincronizada.
     */
    val pendingDelete: Boolean = false,
)