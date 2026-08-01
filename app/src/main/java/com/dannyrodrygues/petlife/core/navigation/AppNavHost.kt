package com.dannyrodrygues.petlife.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dannyrodrygues.petlife.feature.auth.login.LoginScreen
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
                    // Será implementado quando a tela de cadastro existir.
                },
            )
        }

        composable<AppDestination.Login> {
            LoginScreen(
                onLoginClick = {
                    // Será implementado quando criarmos a Home/autenticação.
                },
                onForgotPasswordClick = {
                    // Será implementado quando a tela existir.
                },
                onRegisterClick = {
                    // Será implementado quando a tela existir.
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }
    }
}