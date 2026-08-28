package com.dannyrodrygues.petlife.core.tenant.model

data class BrandConfig(
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val tertiaryColorHex: String? = null,
    val logoPath: String? = null,
    val bannerPath: String? = null,
)