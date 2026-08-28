package com.dannyrodrygues.petlife.core.tenant.config

import com.dannyrodrygues.petlife.core.tenant.model.BrandConfig
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig

object PetLifeDefaultTenant {

    const val TENANT_ID = "petlife"

    val config = TenantConfig(
        id = TENANT_ID,
        name = "PetLife",
        active = true,
        brand = BrandConfig(
            primaryColorHex = "#7656B5",
            secondaryColorHex = "#5ACDDA",
            tertiaryColorHex = "#33B8C8",
            logoPath = null,
            bannerPath = null,
        ),
    )
}