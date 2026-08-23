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

    @Serializable
    data object  AddPet : AppDestination

    @Serializable
    data class PetDetails(
        val petId: Long,
    ) : AppDestination

    @Serializable
    data class EditPet(
        val petId: Long,
    ) : AppDestination

    @Serializable
    data class Vaccines(
        val petId: Long,
    ) : AppDestination

    @Serializable
    data class AddVaccine(
        val petId: Long,
    ) : AppDestination


}