package com.dannyrodrygues.petlife.feature.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dannyrodrygues.petlife.R
import com.dannyrodrygues.petlife.core.components.PetLifeBrandLogo
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val loginViewModel: LoginViewModel = viewModel()

    val uiState by loginViewModel
        .uiState
        .collectAsState()

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(uiState.isLoginSuccessful) {
        if (uiState.isLoginSuccessful) {
            loginViewModel.consumeLoginSuccess()
            onLoginClick()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .navigationBarsPadding()
            .padding(horizontal = PetLifeSpacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp)
                .padding(vertical = PetLifeSpacing.Large),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PetLifeBrandLogo(
                modifier = Modifier.size(100.dp),
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            Text(
                text = stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Small),
            )

            Text(
                text = stringResource(R.string.login_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            OutlinedTextField(
                value = email,
                onValueChange = { newEmail ->
                    email = newEmail
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.email_label),
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.email_placeholder),
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            OutlinedTextField(
                value = password,
                onValueChange = { newPassword ->
                    password = newPassword
                },
                modifier = Modifier
                    .fillMaxWidth(),
                label = {
                    Text(
                        text = stringResource(R.string.password_label),
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.password_placeholder),
                    )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                shape = MaterialTheme.shapes.medium,
            )

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    text = stringResource(R.string.action_forgot_password),
                    color = MaterialTheme.colorScheme.secondary,
                )
            }

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            //pergunta ao Supabase se o e-mail e a senha estão corretos.
            PetLifePrimaryButton(
                text = if (uiState.isLoading) {
                    "Entrando..."
                } else {
                    stringResource(R.string.action_login)
                },
                onClick = {
                    if (!uiState.isLoading) {
                        loginViewModel.signIn(
                            email = email,
                            password = password,
                        )
                    }
                },
            )

            // mostra a mensagem de erro

            uiState.errorMessage?.let { errorMessage ->
                Spacer(
                    modifier = Modifier.height(
                        PetLifeSpacing.Small,
                    ),
                )

                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Small),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.login_no_account),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )

                TextButton(
                    onClick = onRegisterClick,
                ) {
                    Text(
                        text = stringResource(R.string.action_create_account),
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            TextButton(
                onClick = onBackClick,
            ) {
                Text(
                    text = stringResource(R.string.action_back),
                )
            }
        }
    }
}