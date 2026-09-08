package com.dannyrodrygues.petlife.feature.pet.data.remote

import com.dannyrodrygues.petlife.core.data.remote.SupabaseProvider
import com.dannyrodrygues.petlife.feature.pet.data.local.PetDao
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import java.time.Instant

class PetRemoteDataSource(
    private val client: SupabaseClient = SupabaseProvider.client,
) {

    suspend fun getPetsByTenantId(
        tenantId: String,
    ): List<PetDto> {
        return client
            .from("pets")
            .select {
                filter {
                    eq(
                        column = "tenant_id",
                        value = tenantId,
                    )
                }
            }
            .decodeList<PetDto>()
    }

    suspend fun insertPet(
        pet: PetInsertDto,
    ): PetDto {
        return client
            .from("pets")
            .insert(pet) {
                select()
            }
            .decodeSingle<PetDto>()
    }

    suspend fun updatePet(
        remoteId: String,
        tenantId: String,
        pet: PetUpdateDto,
    ): PetDto {
        return client
            .from("pets")
            .update(pet) {
                select()

                filter {
                    eq(
                        column = "id",
                        value = remoteId,
                    )

                    eq(
                        column = "tenant_id",
                        value = tenantId,
                    )
                }
            }
            .decodeSingle<PetDto>()
    }

    suspend fun softDeletePet(
        remoteId: String,
        tenantId: String,
    ): PetDto {
        val deleteDto = PetDao.PetDeleteDto(
            deletedAt = Instant.now().toString(),
        )

        return client
            .from("pets")
            .update(deleteDto) {
                select()

                filter {
                    eq(
                        column = "id",
                        value = remoteId,
                    )

                    eq(
                        column = "tenant_id",
                        value = tenantId,
                    )
                }
            }
            .decodeSingle<PetDto>()
    }
}