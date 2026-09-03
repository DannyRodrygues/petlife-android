package com.dannyrodrygues.petlife.core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileDto(
    val id: String,

    @SerialName("tenant_id")
    val tenantId: String,

    @SerialName("full_name")
    val fullName: String?,

    val role: String,

    val active: Boolean,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,
)