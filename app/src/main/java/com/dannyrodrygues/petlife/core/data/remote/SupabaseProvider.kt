package com.dannyrodrygues.petlife.core.data.remote

import com.dannyrodrygues.petlife.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseProvider {

    val client: SupabaseClient by lazy {

        require(BuildConfig.SUPABASE_URL.isNotBlank()) {
            "SUPABASE_URL não configurada."
        }

        require(
            BuildConfig.SUPABASE_PUBLISHABLE_KEY.isNotBlank(),
        ) {
            "SUPABASE_PUBLISHABLE_KEY não configurada."
        }

        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY,
        ) {
            install(Auth)
            install(Postgrest)
            install(Storage)
        }
    }
}