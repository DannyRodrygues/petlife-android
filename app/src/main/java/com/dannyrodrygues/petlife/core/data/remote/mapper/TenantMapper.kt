package com.dannyrodrygues.petlife.core.data.remote.mapper

import com.dannyrodrygues.petlife.core.data.remote.model.BrandConfigDto
import com.dannyrodrygues.petlife.core.data.remote.model.TenantDto
import com.dannyrodrygues.petlife.core.tenant.model.BrandConfig
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig

fun TenantDto.toTenantConfig(
    brandConfigDto: BrandConfigDto,
): TenantConfig {

    return TenantConfig(
        id = id,
        name = name,
        active = active,
        brand = BrandConfig(
            primaryColorHex = brandConfigDto.primaryColor,
            secondaryColorHex = brandConfigDto.secondaryColor,
            tertiaryColorHex = brandConfigDto.tertiaryColor,
            logoPath = brandConfigDto.logoPath,
            bannerPath = brandConfigDto.bannerPath,
        ),
    )
}