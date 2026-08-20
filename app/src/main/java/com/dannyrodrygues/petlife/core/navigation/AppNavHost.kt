package com.dannyrodrygues.petlife.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dannyrodrygues.petlife.feature.auth.forgotpassword.ForgotPasswordScreen
import com.dannyrodrygues.petlife.feature.auth.login.LoginScreen
import com.dannyrodrygues.petlife.feature.auth.register.RegisterScreen
import com.dannyrodrygues.petlife.feature.home.HomeScreen
import com.dannyrodrygues.petlife.feature.home.HomeViewModel
import com.dannyrodrygues.petlife.feature.home.HomeViewModelFactory
import com.dannyrodrygues.petlife.feature.pet.add.AddPetScreen
import com.dannyrodrygues.petlife.feature.pet.add.AddPetViewModel
import com.dannyrodrygues.petlife.feature.pet.add.AddPetViewModelFactory
import com.dannyrodrygues.petlife.feature.pet.data.local.PetDatabaseProvider
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository
import com.dannyrodrygues.petlife.feature.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Welcome,
        modifier = modifier,
    ) {
        composable<AppDestination.Welcome> {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(AppDestination.Login)
                },
                onRegisterClick = {
                    navController.navigate(AppDestination.Register)
                },
            )
        }

        composable<AppDestination.Login> {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(AppDestination.Home) {
                        popUpTo(AppDestination.Welcome){
                            inclusive =true
                        }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(AppDestination.ForgotPassword)
                },
                onRegisterClick = {
                    navController.navigate(AppDestination.Register)
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable<AppDestination.Register> {
            RegisterScreen(
                onRegisterClick = {
                    // Será implementado quando a autenticação existir.
                },
                onLoginClick = {
                    navController.navigate(AppDestination.Login) {
                        popUpTo(AppDestination.Register) {
                            inclusive = true
                        }
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
        composable<AppDestination.ForgotPassword> {
            ForgotPasswordScreen(
                onSendRecoveryLinkClick = {
                    // Será implementado quando integrarmos a autenticação.
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable<AppDestination.Home> {

            val context = LocalContext.current

            val database = PetDatabaseProvider.getDatabase(context)

            val repository = PetRepository(
                petDao = database.petDao(),
            )

            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(repository),
            )

            val pets by homeViewModel.pets.collectAsState()

            HomeScreen(
                pets = pets,
                onAddPetClick = {
                    navController.navigate(AppDestination.AddPet)
                },
            )
        }

        composable<AppDestination.AddPet> {

            val context = LocalContext.current

            val database = PetDatabaseProvider.getDatabase(context)

            val repository = PetRepository(
                petDao = database.petDao(),
            )

            val addPetViewModel: AddPetViewModel = viewModel(
                factory = AddPetViewModelFactory(repository),
            )

            AddPetScreen(
                onSaveClick = {
                        name,
                        species,
                        breed,
                        gender,
                        birthDate,
                        weight,
                        observations,
                        photoUri,
                    ->

                    addPetViewModel.savePet(
                        name = name,
                        species = species,
                        breed = breed,
                        gender = gender,
                        birthDate = birthDate,
                        weight = weight,
                        observations = observations,
                        photoUri = photoUri,
                        onSaved = {
                            navController.popBackStack()
                        },
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}