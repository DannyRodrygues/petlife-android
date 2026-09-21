package com.dannyrodrygues.petlife.feature.pet.vaccines.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.feature.pet.add.PetDateField
import com.dannyrodrygues.petlife.feature.pet.add.PetFieldLabel
import com.dannyrodrygues.petlife.feature.pet.add.PetObservationsField
import com.dannyrodrygues.petlife.feature.pet.add.PetRequiredTextField
import com.dannyrodrygues.petlife.feature.pet.add.PetTextField
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditVaccineSheet(
    vaccine: VaccineEntity,
    onDismiss: () -> Unit,
    onSave: (VaccineEntity) -> Unit,
) {

    var name by rememberSaveable(vaccine.id) {
        mutableStateOf(vaccine.name)
    }

    var doseDescription by rememberSaveable(vaccine.id) {
        mutableStateOf(
            vaccine.doseDescription.orEmpty(),
        )
    }

    var applicationDateMillis by rememberSaveable(vaccine.id) {
        mutableStateOf(
            vaccine.applicationDateMillis,
        )
    }

    var nextDoseDateMillis by rememberSaveable(vaccine.id) {
        mutableStateOf(
            vaccine.nextDoseDateMillis,
        )
    }

    var observations by rememberSaveable(vaccine.id) {
        mutableStateOf(
            vaccine.observations.orEmpty(),
        )
    }

    var nameError by rememberSaveable(vaccine.id) {
        mutableStateOf(false)
    }

    var applicationDateError by rememberSaveable(vaccine.id) {
        mutableStateOf(false)
    }

    var showApplicationDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    var showNextDoseDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    val applicationDatePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = applicationDateMillis,
        )

    val nextDoseDatePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = nextDoseDateMillis,
        )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(
                    rememberScrollState(),
                )
                .imePadding()
                .navigationBarsPadding()
                .padding(
                    horizontal = PetLifeSpacing.Large,
                    vertical = PetLifeSpacing.Medium,
                ),
        ) {

            Text(
                text = "Editar vacina",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.ExtraSmall,
                ),
            )

            Text(
                text = "Altere somente as informações necessárias.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.Large,
                ),
            )

            /*
             * Nome
             */
            PetFieldLabel(
                text = "Nome da vacina",
                required = true,
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.ExtraSmall,
                ),
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
                modifier = Modifier.height(
                    PetLifeSpacing.Medium,
                ),
            )

            /*
             * Dose
             */
            PetFieldLabel(
                text = "Dose / descrição",
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.ExtraSmall,
                ),
            )

            PetTextField(
                value = doseDescription,
                onValueChange = {
                    doseDescription = it
                },
                placeholder = "Ex.: Dose anual",
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.Medium,
                ),
            )

            /*
             * Data da aplicação
             */
            PetFieldLabel(
                text = "Data da aplicação",
                required = true,
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.ExtraSmall,
                ),
            )

            PetDateField(
                value = applicationDateMillis?.let {
                    formatVaccineDate(it)
                }.orEmpty(),
                onClick = {
                    applicationDateError = false
                    showApplicationDatePicker = true
                },
            )

            if (applicationDateError) {
                Spacer(
                    modifier = Modifier.height(
                        PetLifeSpacing.ExtraSmall,
                    ),
                )

                Text(
                    text = "Informe a data da aplicação.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.Medium,
                ),
            )

            /*
             * Próxima dose
             */
            PetFieldLabel(
                text = "Próxima dose",
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.ExtraSmall,
                ),
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
                modifier = Modifier.height(
                    PetLifeSpacing.Medium,
                ),
            )

            /*
             * Observações
             */
            PetFieldLabel(
                text = "Observações (opcional)",
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.ExtraSmall,
                ),
            )

            PetObservationsField(
                value = observations,
                onValueChange = {
                    observations = it
                },
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.Large,
                ),
            )

            /*
             * Salvar
             */
            PetLifePrimaryButton(
                text = "Salvar alterações",
                onClick = {

                    nameError = name.isBlank()

                    applicationDateError =
                        applicationDateMillis == null

                    if (
                        !nameError &&
                        !applicationDateError
                    ) {

                        onSave(
                            vaccine.copy(
                                name = name.trim(),
                                doseDescription =
                                    doseDescription
                                        .trim()
                                        .takeIf {
                                            it.isNotEmpty()
                                        },
                                applicationDateMillis =
                                    applicationDateMillis,
                                nextDoseDateMillis =
                                    nextDoseDateMillis,
                                observations =
                                    observations
                                        .trim()
                                        .takeIf {
                                            it.isNotEmpty()
                                        },
                            ),
                        )
                    }
                },
                widthFraction = 0.60f,
            )

            Spacer(
                modifier = Modifier.height(
                    PetLifeSpacing.Large,
                ),
            )
        }
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
                            applicationDatePickerState
                                .selectedDateMillis

                        applicationDateError =
                            applicationDateMillis == null

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
                            nextDoseDatePickerState
                                .selectedDateMillis

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