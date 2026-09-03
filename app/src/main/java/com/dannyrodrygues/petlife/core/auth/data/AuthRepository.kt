package com.dannyrodrygues.petlife.core.auth.data

import com.dannyrodrygues.petlife.core.data.remote.AuthRemoteDataSource

class AuthRepository(
    private val remoteDataSource: AuthRemoteDataSource =
        AuthRemoteDataSource(),
) {

    suspend fun signIn(
        email: String,
        password: String,
    ) {
        remoteDataSource.signIn(
            email = email,
            password = password,
        )
    }
}
