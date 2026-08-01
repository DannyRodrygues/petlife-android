package com.dannyrodrygues.petlife

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.dannyrodrygues.petlife.core.navigation.AppNavHost
import com.dannyrodrygues.petlife.ui.theme.PetLifeTheme
import com.dannyrodrygues.petlife.feature.auth.login.LoginScreen

@Composable
fun PetLifeApp() {
    PetLifeTheme {
        val navController = rememberNavController()

        LoginScreen(
            onLoginClick = {},
            onForgotPasswordClick = {},
            onRegisterClick = {},
            onBackClick = {},
        )
    }
}