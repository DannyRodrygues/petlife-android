package com.dannyrodrygues.petlife.feature.pet.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing

@Composable
fun PetPhotoSection(
    imageUri: String?,
    onPhotoClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Foto do pet",
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Small),
            )
        }

        TextButton(
            onClick = onPhotoClick,
        ) {
            Text(
                text = if (imageUri == null) {
                    "📷 Adicionar foto"
                } else {
                    "Alterar foto"
                },
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}

@Composable
fun PetFieldLabel(
    text: String,
    required: Boolean = false,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Medium,
        )

        if (required) {
            Spacer(
                modifier = Modifier.width(2.dp),
            )

            Text(
                text = "*",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
fun PetRequiredTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isError: Boolean,
    errorMessage: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            PetPlaceholder(
                text = placeholder,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        isError = isError,
        supportingText = if (isError) {
            {
                Text(
                    text = errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next,
        ),
        shape = MaterialTheme.shapes.medium,
        colors = petFieldColors(),
    )
}

@Composable
fun PetTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            PetPlaceholder(
                text = placeholder,
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Next,
        ),
        shape = MaterialTheme.shapes.medium,
        colors = petFieldColors(),
    )
}

@Composable
fun PetDropdownField(
    value: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit,
    isError: Boolean,
    errorMessage: String,
    placeholder: String = "Selecione",
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            placeholder = {
                PetPlaceholder(
                    text = placeholder,
                )
            },
            trailingIcon = {
                Text(
                    text = "▼",
                    fontSize = 11.sp,
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                )
            },
            textStyle = MaterialTheme.typography.bodySmall,
            singleLine = true,
            isError = isError,
            supportingText = if (isError) {
                {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            } else {
                null
            },
            shape = MaterialTheme.shapes.medium,
            colors = petFieldColors(),
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    onExpandedChange(true)
                },
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                onExpandedChange(false)
            },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    },
                    onClick = {
                        onOptionSelected(option)
                        onExpandedChange(false)
                    },
                )
            }
        }
    }
}

@Composable
fun PetDateField(
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            placeholder = {
                PetPlaceholder(
                    text = "Selecionar",
                )
            },
            trailingIcon = {
                Text(
                    text = "📅",
                    fontSize = 16.sp,
                )
            },
            textStyle = MaterialTheme.typography.bodySmall,
            singleLine = true,
            shape = MaterialTheme.shapes.medium,
            colors = petFieldColors(),
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable {
                    onClick()
                },
        )
    }
}

@Composable
fun PetObservationsField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            if (newValue.length <= 200) {
                onValueChange(newValue)
            }
        },
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            PetPlaceholder(
                text = "Adicione informações adicionais sobre seu pet",
            )
        },
        textStyle = MaterialTheme.typography.bodyMedium,
        minLines = 3,
        maxLines = 4,
        supportingText = {
            Text(
                text = "${value.length}/200",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodySmall,
            )
        },
        shape = MaterialTheme.shapes.medium,
        colors = petFieldColors(),
    )
}

@Composable
private fun PetPlaceholder(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        maxLines = 1,
    )
}

@Composable
private fun petFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(
            alpha = 0.28f,
        ),
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
        errorBorderColor = MaterialTheme.colorScheme.error,
        errorCursorColor = MaterialTheme.colorScheme.error,
    )