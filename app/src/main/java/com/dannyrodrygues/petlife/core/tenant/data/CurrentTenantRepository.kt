package com.dannyrodrygues.petlife.core.tenant.data

import com.dannyrodrygues.petlife.core.data.remote.ProfileRemoteDataSource
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig

class CurrentTenantRepository(
    private val profileRemoteDataSource: ProfileRemoteDataSource =
        ProfileRemoteDataSource(),

    private val tenantRepository: TenantRepository =
        TenantRepository(),
) {

    suspend fun getCurrentUserTenant(): TenantConfig {
        val profile =
            profileRemoteDataSource.getCurrentUserProfile()

        check(profile.active) {
            "O perfil do usuário está inativo."
        }

        return tenantRepository.getTenantById(
            tenantId = profile.tenantId,
        )
    }
}