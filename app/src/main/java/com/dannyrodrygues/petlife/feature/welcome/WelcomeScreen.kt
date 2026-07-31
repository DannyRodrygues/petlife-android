package com.dannyrodrygues.petlife.feature.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dannyrodrygues.petlife.R
import com.dannyrodrygues.petlife.core.components.PetLifeOutlinedButton
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = PetLifeSpacing.Large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        Text(
            text = stringResource(R.string.welcome_description),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraLarge),
        )

        PetLifePrimaryButton(
            text = stringResource(R.string.action_login),
            onClick = onLoginClick,
            modifier = Modifier.widthIn(max = 320.dp),
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        PetLifeOutlinedButton(
            text = stringResource(R.string.action_register),
            onClick = onRegisterClick,
            modifier = Modifier.widthIn(max = 320.dp),
        )
    }
}