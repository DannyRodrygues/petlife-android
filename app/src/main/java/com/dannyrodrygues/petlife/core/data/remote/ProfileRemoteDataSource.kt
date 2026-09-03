package com.dannyrodrygues.petlife.core.data.remote

import com.dannyrodrygues.petlife.core.data.remote.model.ProfileDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class ProfileRemoteDataSource(
    private val client: SupabaseClient = SupabaseProvider.client,
) {

    suspend fun getCurrentUserProfile(): ProfileDto {
        val currentUser = client
            .auth
            .currentUserOrNull()
            ?: error("Nenhum usuário autenticado.")

        return client
            .from("profiles")
            .select {
                filter {
                    eq("id", currentUser.id)
                }
            }
            .decodeSingle<ProfileDto>()
    }
}