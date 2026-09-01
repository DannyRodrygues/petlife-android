package com.dannyrodrygues.petlife.core.data.remote

import com.dannyrodrygues.petlife.core.data.remote.model.BrandConfigDto
import com.dannyrodrygues.petlife.core.data.remote.model.TenantDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class TenantRemoteDataSource(
    private val client: SupabaseClient = SupabaseProvider.client,
) {

    suspend fun getTenantByName(
        name: String,
    ): TenantDto {

        return client
            .from("tenants")
            .select {
                filter {
                    eq("name", name)
                }
            }
            .decodeSingle<TenantDto>()
    }

    suspend fun getBrandConfigByTenantId(
        tenantId: String,
    ): BrandConfigDto {

        return client
            .from("brand_configs")
            .select {
                filter {
                    eq("tenant_id", tenantId)
                }
            }
            .decodeSingle<BrandConfigDto>()
    }
}