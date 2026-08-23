package com.dannyrodrygues.petlife.feature.pet.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dannyrodrygues.petlife.R
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing

@Composable
fun PetDetailsScreen(
    pet: PetEntity?,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    onVaccinesClick: () -> Unit = {},
    onAppointmentsClick: () -> Unit = {},
    onMedicationsClick: () -> Unit = {},
    onWeightClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->

        if (pet == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Carregando pet...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
        ) {

            /*
             * Banner
             */
            Image(
                painter = painterResource(R.drawable.home_banner),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1080f / 420f),
                contentScale = ContentScale.FillWidth,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(PetLifeSpacing.Large),
            ) {

                /*
                 * Card principal do pet
                 */
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp,
                    ),
                ) {
                    Column(
                        modifier = Modifier.padding(PetLifeSpacing.Medium),
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {

                            if (!pet.photoUri.isNullOrBlank()) {
                                AsyncImage(
                                    model = pet.photoUri,
                                    contentDescription = "Foto de ${pet.name}",
                                    modifier = Modifier
                                        .size(112.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop,
                                )
                            } else {
                                Image(
                                    painter = painterResource(
                                        R.drawable.empty_pet,
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(112.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Fit,
                                )
                            }

                            Spacer(
                                modifier = Modifier.width(
                                    PetLifeSpacing.Medium,
                                ),
                            )

                            Column(
                                modifier = Modifier.weight(1f),
                            ) {
                                Text(
                                    text = pet.name,
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontWeight = FontWeight.Bold,
                                )

                                Spacer(
                                    modifier = Modifier.height(
                                        PetLifeSpacing.Small,
                                    ),
                                )

                                if (!pet.breed.isNullOrBlank()) {
                                    Text(
                                        text = pet.breed,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                    )
                                }

                                Text(
                                    text = pet.species,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }

                        if (!pet.observations.isNullOrBlank()) {
                            Spacer(
                                modifier = Modifier.height(
                                    PetLifeSpacing.Medium,
                                ),
                            )

                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.15f,
                                ),
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    PetLifeSpacing.Medium,
                                ),
                            )

                            Text(
                                text = pet.observations,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.ExtraLarge),
                )

                /*
                 * Saúde e cuidados
                 */
                Text(
                    text = "🐾  Saúde e cuidados",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Medium),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    PetCareCard(
                        icon = "💉",
                        title = "Vacinas",
                        description = "Acompanhe e adicione as vacinas de ${pet.name}",
                        onClick = onVaccinesClick,
                        modifier = Modifier.weight(1f),
                    )

                    PetCareCard(
                        icon = "🩺",
                        title = "Consultas",
                        description = "Histórico de consultas e atendimentos",
                        onClick = onAppointmentsClick,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    PetCareCard(
                        icon = "💊",
                        title = "Medicamentos",
                        description = "Controle os medicamentos e tratamentos",
                        onClick = onMedicationsClick,
                        modifier = Modifier.weight(1f),
                    )

                    PetCareCard(
                        icon = "⚖️",
                        title = "Peso e medidas",
                        description = "Acompanhe o peso e a evolução",
                        onClick = onWeightClick,
                        modifier = Modifier.weight(1f),
                    )
                }

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.ExtraLarge),
                )

                /*
                 * Informações gerais
                 */
                Text(
                    text = "Informações gerais",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Medium),
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp,
                    ),
                ) {
                    Column {
                        PetInfoRow(
                            icon = "📅",
                            label = "Data de nascimento",
                            value = pet.birthDate ?: "Não informada",
                        )

                        HorizontalDivider()

                        PetInfoRow(
                            icon = "⚥",
                            label = "Sexo",
                            value = pet.gender,
                        )

                        HorizontalDivider()

                        PetInfoRow(
                            icon = "🐾",
                            label = "Espécie",
                            value = pet.species,
                        )

                        HorizontalDivider()

                        PetInfoRow(
                            icon = "🛡️",
                            label = "Raça",
                            value = pet.breed ?: "Não informada",
                        )

                        HorizontalDivider()

                        PetInfoRow(
                            icon = "⚖️",
                            label = "Peso atual",
                            value = pet.weight?.let {
                                "${it.toString().replace(".", ",")} kg"
                            } ?: "Não informado",
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Large),
                )

                /*
                 * Editar dados
                 */
                Button(
                    onClick = onEditClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text(
                        text = "✎",
                        style = MaterialTheme.typography.titleLarge,
                    )

                    Spacer(
                        modifier = Modifier.width(PetLifeSpacing.Small),
                    )

                    Text(
                        text = "Editar dados do pet",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

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

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Medium),
                )
            }
        }
    }
}