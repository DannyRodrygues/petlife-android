package com.dannyrodrygues.petlife.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dannyrodrygues.petlife.feature.auth.login.LoginScreen
import com.dannyrodrygues.petlife.feature.auth.register.RegisterScreen
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
                    // Será implementado quando criarmos a Home.
                },
                onForgotPasswordClick = {
                    // Será implementado quando a tela existir.
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
    }
}