package com.dannyrodrygues.petlife.feature.pet.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(
        defaultValue = "'a8d79e94-9a4c-4385-bde1-6bc4b89a4c8a'",
    )
    val tenantId: String =
        "a8d79e94-9a4c-4385-bde1-6bc4b89a4c8a",

    val name: String,
    val species: String,
    val breed: String?,
    val gender: String,
    val birthDate: String?,
    val weight: Double?,
    val observations: String?,
    val photoUri: String?,
)