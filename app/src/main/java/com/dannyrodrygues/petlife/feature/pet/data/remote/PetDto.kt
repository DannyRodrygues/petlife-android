package com.dannyrodrygues.petlife.feature.pet.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PetDto(
    val id: String,

    @SerialName("tenant_id")
    val tenantId: String,

    val name: String,
    val species: String,
    val breed: String?,
    val gender: String,

    @SerialName("birth_date")
    val birthDate: String?,

    val weight: Double?,
    val observations: String?,

    @SerialName("photo_path")
    val photoPath: String?,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("deleted_at")
    val deletedAt: String?,
)

@Serializable
data class PetInsertDto(
    @SerialName("tenant_id")
    val tenantId: String,

    val name: String,
    val species: String,
    val breed: String?,
    val gender: String,

    @SerialName("birth_date")
    val birthDate: String?,

    val weight: Double?,
    val observations: String?,

    @SerialName("photo_path")
    val photoPath: String?,
)

@Serializable
data class PetUpdateDto(
    val name: String,
    val species: String,
    val breed: String?,
    val gender: String,

    @SerialName("birth_date")
    val birthDate: String?,

    val weight: Double?,
    val observations: String?,

    @SerialName("photo_path")
    val photoPath: String?,
)