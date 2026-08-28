package com.dannyrodrygues.petlife.core.tenant.config

import com.dannyrodrygues.petlife.core.tenant.model.BrandConfig
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig

object TestTenant {

    const val TENANT_ID = "test-company"

    val config = TenantConfig(
        id = TENANT_ID,
        name = "Clínica Bicho Feliz",
        active = true,
        brand = BrandConfig(
            primaryColorHex = "#1565C0",
            secondaryColorHex = "#26A69A",
            tertiaryColorHex = "#00897B",
            logoPath = null,
            bannerPath = null,
        ),
    )
}