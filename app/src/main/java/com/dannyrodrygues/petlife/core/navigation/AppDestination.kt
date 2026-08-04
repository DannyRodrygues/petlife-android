package com.dannyrodrygues.petlife.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppDestination {

    @Serializable
    data object Welcome : AppDestination

    @Serializable
    data object Login : AppDestination

    @Serializable
    data object Register : AppDestination

    @Serializable
    data object ForgotPassword : AppDestination

    @Serializable
    data object  Home : AppDestination
}