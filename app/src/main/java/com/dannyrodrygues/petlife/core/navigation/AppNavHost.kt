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
import androidx.navigation.toRoute
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
import com.dannyrodrygues.petlife.feature.pet.details.PetDetailsScreen
import com.dannyrodrygues.petlife.feature.pet.details.PetDetailsViewModel
import com.dannyrodrygues.petlife.feature.pet.details.PetDetailsViewModelFactory
import com.dannyrodrygues.petlife.feature.pet.edit.EditPetScreen
import com.dannyrodrygues.petlife.feature.pet.edit.EditPetViewModel
import com.dannyrodrygues.petlife.feature.pet.edit.EditPetViewModelFactory
import com.dannyrodrygues.petlife.feature.pet.vaccines.VaccinesScreen
import com.dannyrodrygues.petlife.feature.pet.vaccines.VaccinesViewModel
import com.dannyrodrygues.petlife.feature.pet.vaccines.VaccinesViewModelFactory
import com.dannyrodrygues.petlife.feature.pet.vaccines.add.AddVaccineScreen
import com.dannyrodrygues.petlife.feature.pet.vaccines.add.AddVaccineViewModel
import com.dannyrodrygues.petlife.feature.pet.vaccines.add.AddVaccineViewModelFactory
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository.VaccineRepository
import com.dannyrodrygues.petlife.feature.welcome.WelcomeScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    onLoginSuccess: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Welcome,
        modifier = modifier,
    ) {

        // Welcome
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

        // Login
        composable<AppDestination.Login> {
            LoginScreen(
                onLoginClick = {
                    onLoginSuccess()

                    navController.navigate(AppDestination.Home) {
                        popUpTo(AppDestination.Welcome) {
                            inclusive = true
                        }
                    }
                },
                onForgotPasswordClick = {
                    navController.navigate(
                        AppDestination.ForgotPassword,
                    )
                },
                onRegisterClick = {
                    navController.navigate(
                        AppDestination.Register,
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        // Cadastro
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

        // Recuperação de senha
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

        // Home
        composable<AppDestination.Home> {

            val context = LocalContext.current

            val database =
                PetDatabaseProvider.getDatabase(context)

            val repository = PetRepository(
                petDao = database.petDao(),
            )

            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModelFactory(
                    repository = repository,
                ),
            )

            val pets by homeViewModel.pets.collectAsState()

            HomeScreen(
                pets = pets,
                onAddPetClick = {
                    navController.navigate(
                        AppDestination.AddPet,
                    )
                },
                onPetClick = { petId ->
                    navController.navigate(
                        AppDestination.PetDetails(
                            petId = petId,
                        ),
                    )
                },
            )
        }

        // Cadastrar pet
        composable<AppDestination.AddPet> {

            val context = LocalContext.current

            val database =
                PetDatabaseProvider.getDatabase(context)

            val repository = PetRepository(
                petDao = database.petDao(),
            )

            val addPetViewModel: AddPetViewModel = viewModel(
                factory = AddPetViewModelFactory(
                    repository = repository,
                ),
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

        // Detalhes do pet
        composable<AppDestination.PetDetails> { backStackEntry ->

            val destination =
                backStackEntry
                    .toRoute<AppDestination.PetDetails>()

            val context = LocalContext.current

            val database =
                PetDatabaseProvider.getDatabase(context)

            val repository = PetRepository(
                petDao = database.petDao(),
            )

            val petDetailsViewModel: PetDetailsViewModel =
                viewModel(
                    factory = PetDetailsViewModelFactory(
                        repository = repository,
                        petId = destination.petId,
                    ),
                )

            val pet by
            petDetailsViewModel.pet.collectAsState()

            PetDetailsScreen(
                pet = pet,
                onBackClick = {
                    navController.popBackStack()
                },
                onVaccinesClick = {
                    navController.navigate(
                        AppDestination.Vaccines(
                            petId = destination.petId,
                        ),
                    )
                },
                onEditClick = {
                    navController.navigate(
                        AppDestination.EditPet(
                            petId = destination.petId,
                        ),
                    )
                },
            )
        }



        // Vacinas
        composable<AppDestination.Vaccines> { backStackEntry ->

            val destination =
                backStackEntry
                    .toRoute<AppDestination.Vaccines>()

            val context = LocalContext.current

            val database =
                PetDatabaseProvider.getDatabase(context)

            val vaccineRepository = VaccineRepository(
                vaccineDao = database.vaccineDao(),
            )

            val petRepository = PetRepository(
                petDao = database.petDao(),
            )

            val vaccinesViewModel: VaccinesViewModel = viewModel(
                factory = VaccinesViewModelFactory(
                    vaccineRepository = vaccineRepository,
                    petRepository = petRepository,
                    petId = destination.petId,
                ),
            )

            val vaccines by vaccinesViewModel
                .vaccines
                .collectAsState()

            val pet by vaccinesViewModel
                .pet
                .collectAsState()

            VaccinesScreen(
                pet = pet,
                vaccines = vaccines,
                onAddVaccineClick = {
                    navController.navigate(
                        AppDestination.AddVaccine(
                            petId = destination.petId,
                        ),
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        //add vaccines
        composable<AppDestination.AddVaccine> { backStackEntry ->

            val destination =
                backStackEntry
                    .toRoute<AppDestination.AddVaccine>()

            val context = LocalContext.current

            val database =
                PetDatabaseProvider.getDatabase(context)

            val repository = VaccineRepository(
                vaccineDao = database.vaccineDao(),
            )

            val addVaccineViewModel: AddVaccineViewModel = viewModel(
                factory = AddVaccineViewModelFactory(
                    repository = repository,
                ),
            )

            AddVaccineScreen(
                petId = destination.petId,
                onSaveClick = {
                        name,
                        doseDescription,
                        applicationDateMillis,
                        nextDoseDateMillis,
                        observations,
                    ->

                    addVaccineViewModel.saveVaccine(
                        petId = destination.petId,
                        name = name,
                        doseDescription = doseDescription,
                        applicationDateMillis = applicationDateMillis,
                        nextDoseDateMillis = nextDoseDateMillis,
                        observations = observations,
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

        //edit pet
        composable<AppDestination.EditPet> { backStackEntry ->

            val destination =
                backStackEntry
                    .toRoute<AppDestination.EditPet>()

            val context = LocalContext.current

            val database =
                PetDatabaseProvider.getDatabase(context)

            val repository = PetRepository(
                petDao = database.petDao(),
            )

            val editPetViewModel: EditPetViewModel = viewModel(
                factory = EditPetViewModelFactory(
                    repository = repository,
                    petId = destination.petId,
                ),
            )

            val pet by editPetViewModel
                .pet
                .collectAsState()

            EditPetScreen(
                pet = pet,
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

                    pet?.let { currentPet ->

                        editPetViewModel.updatePet(
                            currentPet = currentPet,
                            name = name,
                            species = species,
                            breed = breed,
                            gender = gender,
                            birthDate = birthDate,
                            weight = weight,
                            observations = observations,
                            photoUri = photoUri,
                            onUpdated = {
                                navController.popBackStack()
                            },
                        )
                    }
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}