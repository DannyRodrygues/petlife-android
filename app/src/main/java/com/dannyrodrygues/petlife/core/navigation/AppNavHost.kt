package com.dannyrodrygues.petlife.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                onLoginClick = {},
                onRegisterClick = {},
            )
        }
    }
}
