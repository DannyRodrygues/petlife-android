package com.dannyrodrygues.petlife.feature.pet.vaccines.data.remote

import com.dannyrodrygues.petlife.core.data.remote.SupabaseProvider
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.time.Instant

class VaccineRemoteDataSource(
    private val client: SupabaseClient = SupabaseProvider.client,
) {

    suspend fun getVaccinesByTenantId(
        tenantId: String,
    ): List<VaccineDto> {
        return client
            .from("vaccines")
            .select {
                filter {
                    eq(
                        column = "tenant_id",
                        value = tenantId,
                    )
                }
            }
            .decodeList<VaccineDto>()
    }

    suspend fun insertVaccine(
        vaccine: VaccineInsertDto,
    ): VaccineDto {
        return client
            .from("vaccines")
            .insert(vaccine) {
                select()
            }
            .decodeSingle<VaccineDto>()
    }

    suspend fun updateVaccine(
        vaccineId: String,
        tenantId: String,
        vaccine: VaccineUpdateDto,
    ): VaccineDto {
        return client
            .from("vaccines")
            .update(vaccine) {
                filter {
                    eq(
                        column = "id",
                        value = vaccineId,
                    )
                    eq(
                        column = "tenant_id",
                        value = tenantId,
                    )
                }

                select()
            }
            .decodeSingle<VaccineDto>()
    }

    suspend fun softDeleteVaccine(
        vaccineId: String,
        tenantId: String,
    ) {
        client
            .from("vaccines")
            .update(
                VaccineDeleteDto(
                    deletedAt = Instant.now().toString(),
                ),
            ) {
                filter {
                    eq(
                        column = "id",
                        value = vaccineId,
                    )
                    eq(
                        column = "tenant_id",
                        value = tenantId,
                    )
                }
            }
    }
}