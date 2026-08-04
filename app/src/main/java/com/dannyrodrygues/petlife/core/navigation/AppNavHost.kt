package com.dannyrodrygues.petlife.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dannyrodrygues.petlife.feature.auth.login.LoginScreen
import com.dannyrodrygues.petlife.feature.auth.register.RegisterScreen
import com.dannyrodrygues.petlife.feature.welcome.WelcomeScreen
import com.dannyrodrygues.petlife.feature.auth.forgotpassword.ForgotPasswordScreen
import com.dannyrodrygues.petlife.feature.home.HomeScreen

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
            HomeScreen(
                onAddPetClick = {
                    // Será implementado quando criarmos o cadastro de pets.
                }
            )
        }
    }
}