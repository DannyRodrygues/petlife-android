package com.dannyrodrygues.petlife.core.data.remote

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email

class AuthRemoteDataSource {

    suspend fun signIn(
        email: String,
        password: String,
    ) {
        SupabaseProvider
            .client
            .auth
            .signInWith(Email) {
                this.email = email
                this.password = password
            }
    }
}