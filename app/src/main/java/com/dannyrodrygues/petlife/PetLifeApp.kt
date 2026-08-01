package com.dannyrodrygues.petlife

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.dannyrodrygues.petlife.core.navigation.AppNavHost
import com.dannyrodrygues.petlife.ui.theme.PetLifeTheme

@Composable
fun PetLifeApp() {
    PetLifeTheme {
        val navController = rememberNavController()

        AppNavHost(
            navController = navController,
        )
    }
}