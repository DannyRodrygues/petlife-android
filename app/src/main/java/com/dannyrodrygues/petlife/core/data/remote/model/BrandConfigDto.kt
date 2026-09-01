package com.dannyrodrygues.petlife.core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrandConfigDto(

    @SerialName("tenant_id")
    val tenantId: String,

    @SerialName("primary_color")
    val primaryColor: String,

    @SerialName("secondary_color")
    val secondaryColor: String,

    @SerialName("tertiary_color")
    val tertiaryColor: String?,

    @SerialName("logo_path")
    val logoPath: String?,

    @SerialName("banner_path")
    val bannerPath: String?,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,
)