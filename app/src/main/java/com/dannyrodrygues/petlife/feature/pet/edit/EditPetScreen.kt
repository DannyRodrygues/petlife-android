package com.dannyrodrygues.petlife.feature.pet.edit

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.dannyrodrygues.petlife.core.components.PetLifeBrandBanner
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.feature.pet.add.PetDateField
import com.dannyrodrygues.petlife.feature.pet.add.PetDropdownField
import com.dannyrodrygues.petlife.feature.pet.add.PetFieldLabel
import com.dannyrodrygues.petlife.feature.pet.add.PetObservationsField
import com.dannyrodrygues.petlife.feature.pet.add.PetPhotoSection
import com.dannyrodrygues.petlife.feature.pet.add.PetRequiredTextField
import com.dannyrodrygues.petlife.feature.pet.add.PetTextField
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.local.PetImageStorage
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPetScreen(
    pet: PetEntity?,
    onSaveClick: (
        name: String,
        species: String,
        breed: String?,
        gender: String,
        birthDate: String?,
        weight: Double?,
        observations: String?,
        photoUri: String?,
    ) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (pet == null) {
        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(
                modifier = Modifier.height(PetLifeSpacing.ExtraLarge),
            )

            Text(
                text = "Carregando dados do pet...",
                style = MaterialTheme.typography.bodyLarge,
            )
        }

        return
    }

    val context = LocalContext.current

    var name by rememberSaveable(pet.id) {
        mutableStateOf(pet.name)
    }

    var species by rememberSaveable(pet.id) {
        mutableStateOf(pet.species)
    }

    var breed by rememberSaveable(pet.id) {
        mutableStateOf(pet.breed.orEmpty())
    }

    var gender by rememberSaveable(pet.id) {
        mutableStateOf(pet.gender)
    }

    var birthDate by rememberSaveable(pet.id) {
        mutableStateOf(pet.birthDate.orEmpty())
    }

    var weight by rememberSaveable(pet.id) {
        mutableStateOf(
            pet.weight
                ?.toString()
                ?.replace(".", ",")
                .orEmpty(),
        )
    }

    var observations by rememberSaveable(pet.id) {
        mutableStateOf(pet.observations.orEmpty())
    }

    var selectedImageUri by rememberSaveable(pet.id) {
        mutableStateOf(pet.photoUri)
    }

    var speciesExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var genderExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    var showDatePicker by rememberSaveable {
        mutableStateOf(false)
    }

    var nameError by rememberSaveable {
        mutableStateOf(false)
    }

    var speciesError by rememberSaveable {
        mutableStateOf(false)
    }

    var genderError by rememberSaveable {
        mutableStateOf(false)
    }

    val speciesOptions = listOf(
        "Cachorro",
        "Gato",
        "Pássaro",
        "Coelho",
        "Outro",
    )

    val genderOptions = listOf(
        "Macho",
        "Fêmea",
    )

    val datePickerState = rememberDatePickerState()

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        if (uri != null) {
            selectedImageUri = PetImageStorage.saveImage(
                context = context,
                sourceUri = uri,
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .navigationBarsPadding(),
    ) {

        // Banner
        PetLifeBrandBanner()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = PetLifeSpacing.Large,
                    vertical = PetLifeSpacing.Medium,
                ),
        ) {

            // Título
            Text(
                text = "Editar dados do pet",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
            )

            Text(
                text = "Atualize as informações de ${pet.name}.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Large),
            )

            // Foto
            PetPhotoSection(
                imageUri = selectedImageUri,
                onPhotoClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly,
                        ),
                    )
                },
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            // Nome
            PetFieldLabel(
                text = "Nome do pet",
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
                placeholder = "Digite o nome do pet",
                isError = nameError,
                errorMessage = "Informe o nome do pet.",
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            // Espécie + Raça
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    PetFieldLabel(
                        text = "Espécie",
                        required = true,
                    )

                    Spacer(
                        modifier = Modifier.height(
                            PetLifeSpacing.ExtraSmall,
                        ),
                    )

                    PetDropdownField(
                        value = species,
                        options = speciesOptions,
                        expanded = speciesExpanded,
                        onExpandedChange = {
                            speciesExpanded = it
                        },
                        onOptionSelected = {
                            species = it
                            speciesError = false
                        },
                        isError = speciesError,
                        errorMessage = "Selecione a espécie.",
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp),
                )

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    PetFieldLabel(
                        text = "Raça",
                    )

                    Spacer(
                        modifier = Modifier.height(
                            PetLifeSpacing.ExtraSmall,
                        ),
                    )

                    PetTextField(
                        value = breed,
                        onValueChange = {
                            breed = it
                        },
                        placeholder = "Digite a raça",
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            // Sexo + Data
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    PetFieldLabel(
                        text = "Sexo",
                        required = true,
                    )

                    Spacer(
                        modifier = Modifier.height(
                            PetLifeSpacing.ExtraSmall,
                        ),
                    )

                    PetDropdownField(
                        value = gender,
                        options = genderOptions,
                        expanded = genderExpanded,
                        onExpandedChange = {
                            genderExpanded = it
                        },
                        onOptionSelected = {
                            gender = it
                            genderError = false
                        },
                        isError = genderError,
                        errorMessage = "Selecione o sexo.",
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp),
                )

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    PetFieldLabel(
                        text = "Data de nascimento",
                    )

                    Spacer(
                        modifier = Modifier.height(
                            PetLifeSpacing.ExtraSmall,
                        ),
                    )

                    PetDateField(
                        value = birthDate,
                        onClick = {
                            showDatePicker = true
                        },
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            // Peso
            PetFieldLabel(
                text = "Peso (kg)",
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
            )

            PetTextField(
                value = weight,
                onValueChange = {
                    weight = it
                },
                placeholder = "Ex.: 8,5",
                keyboardType = KeyboardType.Decimal,
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )

            // Observações
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

            // Salvar
            PetLifePrimaryButton(
                text = "Salvar alterações",
                onClick = {
                    nameError = name.isBlank()
                    speciesError = species.isBlank()
                    genderError = gender.isBlank()

                    val formIsValid =
                        !nameError &&
                                !speciesError &&
                                !genderError

                    if (formIsValid) {
                        val parsedWeight = weight
                            .replace(",", ".")
                            .toDoubleOrNull()

                        onSaveClick(
                            name,
                            species,
                            breed.takeIf {
                                it.isNotBlank()
                            },
                            gender,
                            birthDate.takeIf {
                                it.isNotBlank()
                            },
                            parsedWeight,
                            observations.takeIf {
                                it.isNotBlank()
                            },
                            selectedImageUri,
                        )
                    }
                },
            )

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Small),
            )

            // Voltar
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

            Spacer(
                modifier = Modifier.height(PetLifeSpacing.Medium),
            )
        }
    }

    // DatePicker
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->

                            val selectedDate = Instant
                                .ofEpochMilli(millis)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()

                            birthDate = selectedDate.format(
                                DateTimeFormatter.ofPattern(
                                    "dd/MM/yyyy",
                                ),
                            )
                        }

                        showDatePicker = false
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
                        showDatePicker = false
                    },
                ) {
                    Text(
                        text = "Cancelar",
                    )
                }
            },
        ) {
            DatePicker(
                state = datePickerState,
            )
        }
    }
}