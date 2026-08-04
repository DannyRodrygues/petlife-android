package com.dannyrodrygues.petlife.feature.auth.register

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.dannyrodrygues.petlife.R
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing

@Composable
fun RegisterScreen(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by rememberSaveable {
        mutableStateOf("")
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }

    var confirmPassword by rememberSaveable {
        mutableStateOf("")
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
            Image(
                painter = painterResource(R.drawable.logo_petlife),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(80.dp),
            )

            Spacer(
                modifier = Modifier.height(4.dp),
            )

            Text(
                text = stringResource(R.string.register_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Small),
            )

            Text(
                text = stringResource(R.string.register_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            OutlinedTextField(
                value = name,
                onValueChange = { newName ->
                    name = newName
                },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodySmall,
                label = {
                    Text(
                        text = stringResource(R.string.name_label),
                        style = MaterialTheme.typography.labelSmall,
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.name_placeholder),
                        style = MaterialTheme.typography.bodySmall,
                    )
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                shape = MaterialTheme.shapes.medium,
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
                textStyle = MaterialTheme.typography.bodySmall,

                label = {
                    Text(
                        text = stringResource(R.string.email_label),
                        style = MaterialTheme.typography.labelSmall,

                        )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.email_placeholder),
                        style = MaterialTheme.typography.bodySmall,


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
                textStyle = MaterialTheme.typography.bodySmall,

                label = {
                    Text(
                        text = stringResource(R.string.password_label),
                        style = MaterialTheme.typography.labelSmall,

                        )
                },
                placeholder = {
                    Text(
                        text = stringResource(R.string.password_placeholder),
                        style = MaterialTheme.typography.bodyMedium,

                        )
                },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next,
                ),
                shape = MaterialTheme.shapes.medium,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { newConfirmPassword ->
                    confirmPassword = newConfirmPassword
                },
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.bodySmall,

                label = {
                    Text(
                        text = stringResource(R.string.confirm_password_label),
                        style = MaterialTheme.typography.labelSmall,

                        )
                },
                placeholder = {
                    Text(
                        text = stringResource(
                            R.string.confirm_password_placeholder),
                        style = MaterialTheme.typography.bodySmall,

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

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Large),
            )

            PetLifePrimaryButton(
                text = stringResource(R.string.action_register),
                onClick = onRegisterClick,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Small),
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.register_has_account),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )

                TextButton(
                    onClick = onLoginClick,
                ) {
                    Text(
                        text = stringResource(R.string.action_go_to_login),
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

