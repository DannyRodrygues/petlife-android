package com.dannyrodrygues.petlife.feature.pet.vaccines.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVaccineScreen(
    petId: Long,
    onSaveClick: (
        name: String,
        doseDescription: String?,
        applicationDateMillis: Long?,
        nextDoseDateMillis: Long?,
        observations: String?,
    ) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by rememberSaveable {
        mutableStateOf("")
    }

    var doseDescription by rememberSaveable {
        mutableStateOf("")
    }

    var applicationDateMillis by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    var nextDoseDateMillis by rememberSaveable {
        mutableStateOf<Long?>(null)
    }

    var observations by rememberSaveable {
        mutableStateOf("")
    }

    var nameError by rememberSaveable {
        mutableStateOf(false)
    }

    var showApplicationDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    var showNextDoseDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    val applicationDatePickerState = rememberDatePickerState()
    val nextDoseDatePickerState = rememberDatePickerState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .navigationBarsPadding()
            .padding(
                horizontal = PetLifeSpacing.Large,
                vertical = PetLifeSpacing.Large,
            ),
    ) {

        Text(
            text = "Adicionar vacina",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        Text(
            text = "Registre as informações da vacina do pet.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraLarge),
        )

        /*
         * Nome da vacina
         */
        Text(
            text = "Nome da vacina *",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it

                if (it.isNotBlank()) {
                    nameError = false
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Ex.: V10, Raiva, Giárdia",
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            singleLine = true,
            isError = nameError,
            supportingText = if (nameError) {
                {
                    Text(
                        text = "Informe o nome da vacina.",
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
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        /*
         * Dose / descrição
         */
        Text(
            text = "Dose / descrição",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        OutlinedTextField(
            value = doseDescription,
            onValueChange = {
                doseDescription = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Ex.: Dose anual",
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

        /*
         * Data da aplicação
         */
        Text(
            text = "Data da aplicação",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        TextButton(
            onClick = {
                showApplicationDatePicker = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = applicationDateMillis?.let {
                    formatVaccineDate(it)
                } ?: "📅 Selecionar data da aplicação",
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        /*
         * Próxima dose
         */
        Text(
            text = "Próxima dose",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        TextButton(
            onClick = {
                showNextDoseDatePicker = true
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = nextDoseDateMillis?.let {
                    formatVaccineDate(it)
                } ?: "📅 Selecionar próxima dose",
                color = MaterialTheme.colorScheme.secondary,
            )
        }

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        /*
         * Observações
         */
        Text(
            text = "Observações (opcional)",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        OutlinedTextField(
            value = observations,
            onValueChange = { newValue ->
                if (newValue.length <= 200) {
                    observations = newValue
                }
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Adicione informações importantes sobre a vacina",
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            minLines = 3,
            maxLines = 4,
            supportingText = {
                Text(
                    text = "${observations.length}/200",
                )
            },
            shape = MaterialTheme.shapes.medium,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Large),
        )

        /*
         * Salvar
         */
        PetLifePrimaryButton(
            text = "Salvar vacina",
            onClick = {
                nameError = name.isBlank()

                if (!nameError) {
                    onSaveClick(
                        name.trim(),
                        doseDescription
                            .trim()
                            .takeIf { it.isNotEmpty() },
                        applicationDateMillis,
                        nextDoseDateMillis,
                        observations
                            .trim()
                            .takeIf { it.isNotEmpty() },
                    )
                }
            },
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Small),
        )

        /*
         * Voltar
         */
        TextButton(
            onClick = onBackClick,
            modifier = Modifier.align(
                Alignment.CenterHorizontally,
            ),
        ) {
            Text(
                text = "Voltar",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            )
        }
    }

    /*
     * DatePicker - Data da aplicação
     */
    if (showApplicationDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showApplicationDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        applicationDateMillis =
                            applicationDatePickerState.selectedDateMillis

                        showApplicationDatePicker = false
                    },
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showApplicationDatePicker = false
                    },
                ) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(
                state = applicationDatePickerState,
            )
        }
    }

    /*
     * DatePicker - Próxima dose
     */
    if (showNextDoseDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showNextDoseDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        nextDoseDateMillis =
                            nextDoseDatePickerState.selectedDateMillis

                        showNextDoseDatePicker = false
                    },
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNextDoseDatePicker = false
                    },
                ) {
                    Text("Cancelar")
                }
            },
        ) {
            DatePicker(
                state = nextDoseDatePickerState,
            )
        }
    }
}

private fun formatVaccineDate(
    millis: Long,
): String {
    val date = Instant
        .ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()

    return date.format(
        DateTimeFormatter.ofPattern(
            "dd/MM/yyyy",
        ),
    )
}