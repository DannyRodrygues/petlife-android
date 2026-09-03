package com.dannyrodrygues.petlife.core.tenant.data

import com.dannyrodrygues.petlife.core.data.remote.TenantRemoteDataSource
import com.dannyrodrygues.petlife.core.data.remote.mapper.toTenantConfig
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig

class TenantRepository(
    private val remoteDataSource: TenantRemoteDataSource =
        TenantRemoteDataSource(),
) {

    suspend fun getTenantByName(
        name: String,
    ): TenantConfig {

        val tenantDto =
            remoteDataSource.getTenantByName(name,)

        val brandConfigDto =
            remoteDataSource.getBrandConfigByTenantId(tenantDto.id,)

        return tenantDto.toTenantConfig(brandConfigDto,)
    }

    suspend fun getTenantById(
        tenantId: String,
    ): TenantConfig {
        val tenantDto =
            remoteDataSource.getTenantById(
                tenantId,
            )

        val brandConfigDto =
            remoteDataSource.getBrandConfigByTenantId(
                tenantDto.id,
            )

        return tenantDto.toTenantConfig(
            brandConfigDto,
        )
    }
}

