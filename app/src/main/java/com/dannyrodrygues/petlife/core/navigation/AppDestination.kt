package com.dannyrodrygues.petlife.core.navigation

import kotlinx.serialization.Serializable

sealed interface AppDestination {

    @Serializable
    data object Welcome : AppDestination

    @Serializable
    data object Login : AppDestination
}