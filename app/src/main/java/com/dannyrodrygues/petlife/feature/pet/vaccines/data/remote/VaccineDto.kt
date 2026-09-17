package com.dannyrodrygues.petlife.feature.pet.vaccines.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VaccineDto(
    val id: String,

    @SerialName("tenant_id")
    val tenantId: String,

    @SerialName("pet_id")
    val petId: String,

    val name: String,

    @SerialName("dose_description")
    val doseDescription: String?,

    @SerialName("application_date")
    val applicationDate: String,

    @SerialName("next_dose_date")
    val nextDoseDate: String?,

    val observations: String?,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,

    @SerialName("deleted_at")
    val deletedAt: String?,
)

@Serializable
data class VaccineInsertDto(
    @SerialName("tenant_id")
    val tenantId: String,

    @SerialName("pet_id")
    val petId: String,

    val name: String,

    @SerialName("dose_description")
    val doseDescription: String?,

    @SerialName("application_date")
    val applicationDate: String,

    @SerialName("next_dose_date")
    val nextDoseDate: String?,

    val observations: String?,
)

@Serializable
data class VaccineUpdateDto(
    val name: String,

    @SerialName("dose_description")
    val doseDescription: String?,

    @SerialName("application_date")
    val applicationDate: String,

    @SerialName("next_dose_date")
    val nextDoseDate: String?,

    val observations: String?,
)

@Serializable
data class VaccineDeleteDto(
    @SerialName("deleted_at")
    val deletedAt: String,
)