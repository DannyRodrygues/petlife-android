package com.dannyrodrygues.petlife.feature.pet.vaccines.add

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.feature.pet.add.PetDateField
import com.dannyrodrygues.petlife.feature.pet.add.PetFieldLabel
import com.dannyrodrygues.petlife.feature.pet.add.PetObservationsField
import com.dannyrodrygues.petlife.feature.pet.add.PetRequiredTextField
import com.dannyrodrygues.petlife.feature.pet.add.PetTextField
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
                vertical = PetLifeSpacing.Medium,
            ),
    ) {

        /*
         * Cabeçalho
         */
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            TextButton(
                onClick = onBackClick,
            ) {
                Text(
                    text = "←",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Text(
                text = "Adicionar vacina",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Small),
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
         * Nome
         */
        PetFieldLabel(
            text = "Nome da vacina",
            required = true,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        PetRequiredTextField(
            value = name,
            onValueChange = {
                name = it

                if (it.isNotBlank()) {
                    nameError = false
                }
            },
            placeholder = "Ex.: V10 (Polivalente)",
            isError = nameError,
            errorMessage = "Informe o nome da vacina.",
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        /*
         * Dose
         */
        PetFieldLabel(
            text = "Dose / descrição",
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        PetTextField(
            value = doseDescription,
            onValueChange = {
                doseDescription = it
            },
            placeholder = "Ex.: Dose anual",
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        /*
         * Data da aplicação
         */
        PetFieldLabel(
            text = "Data da aplicação",
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        PetDateField(
            value = applicationDateMillis?.let {
                formatVaccineDate(it)
            }.orEmpty(),
            onClick = {
                showApplicationDatePicker = true
            },
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        /*
         * Próxima dose
         */
        PetFieldLabel(
            text = "Próxima dose",
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        PetDateField(
            value = nextDoseDateMillis?.let {
                formatVaccineDate(it)
            }.orEmpty(),
            onClick = {
                showNextDoseDatePicker = true
            },
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )

        /*
         * Observações
         */
        PetFieldLabel(
            text = "Observações (opcional)",
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
        )

        PetObservationsField(
            value = observations,
            onValueChange = {
                observations = it
            },
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
                            .takeIf {
                                it.isNotEmpty()
                            },

                        applicationDateMillis,

                        nextDoseDateMillis,

                        observations
                            .trim()
                            .takeIf {
                                it.isNotEmpty()
                            },
                    )
                }
            },
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Medium),
        )
    }

    /*
     * DatePicker - Aplicação
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
                    Text(
                        text = "Confirmar",
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showApplicationDatePicker = false
                    },
                ) {
                    Text(
                        text = "Cancelar",
                    )
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
                    Text(
                        text = "Confirmar",
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showNextDoseDatePicker = false
                    },
                ) {
                    Text(
                        text = "Cancelar",
                    )
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