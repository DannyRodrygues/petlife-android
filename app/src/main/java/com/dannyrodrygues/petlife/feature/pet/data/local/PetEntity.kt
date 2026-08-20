package com.dannyrodrygues.petlife.feature.pet.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,
    val species: String,
    val breed: String?,
    val gender: String,
    val birthDate: String?,
    val weight: Double?,
    val observations: String?,
    val photoUri: String?,
)

