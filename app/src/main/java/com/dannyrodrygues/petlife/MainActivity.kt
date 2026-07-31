package com.dannyrodrygues.petlife

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dannyrodrygues.petlife.feature.welcome.WelcomeScreen
import com.dannyrodrygues.petlife.ui.theme.PetLifeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            PetLifeTheme {
                WelcomeScreen(
                    onLoginClick = {},
                    onRegisterClick = {},
                )
            }
        }
    }
}