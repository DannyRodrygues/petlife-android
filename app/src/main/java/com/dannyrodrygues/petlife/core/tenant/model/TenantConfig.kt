package com.dannyrodrygues.petlife.core.tenant.model

data class TenantConfig(
    val id: String,
    val name: String,
    val active: Boolean,
    val brand: BrandConfig,
)