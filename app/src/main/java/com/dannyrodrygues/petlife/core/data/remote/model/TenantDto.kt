package com.dannyrodrygues.petlife.core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TenantDto(
    val id: String,
    val name: String,
    val active: Boolean,

    @SerialName("created_at")
    val createdAt: String,

    @SerialName("updated_at")
    val updatedAt: String,
)