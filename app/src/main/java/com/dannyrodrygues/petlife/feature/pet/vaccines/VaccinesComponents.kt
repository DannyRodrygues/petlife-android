package com.dannyrodrygues.petlife.feature.pet.vaccines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun VaccineReminderCard(
    petName: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.06f,
            ),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PetLifeSpacing.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "🛡️",
                    style = MaterialTheme.typography.headlineSmall,
                )
            }

            Spacer(
                modifier = Modifier.width(PetLifeSpacing.Medium),
            )

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "Mantenha as vacinas do $petName sempre em dia",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(
                    modifier = Modifier.height(4.dp),
                )

                Text(
                    text = "A vacinação em dia protege seu pet e contribui para uma vida mais saudável.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            Text(
                text = "🐾",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.25f,
                ),
            )
        }
    }
}

@Composable
fun VaccineSectionTitle(
    icon: String,
    title: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = icon,
            style = MaterialTheme.typography.bodyLarge,
        )

        Spacer(
            modifier = Modifier.width(PetLifeSpacing.Small),
        )

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
fun NextVaccineCard(
    vaccine: VaccineEntity,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.05f,
            ),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PetLifeSpacing.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.12f,
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "🔔",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Spacer(
                modifier = Modifier.width(PetLifeSpacing.Medium),
            )

            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = vaccine.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                )

                Text(
                    text = "Próxima dose em",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                vaccine.nextDoseDateMillis?.let { millis ->
                    Text(
                        text = formatVaccineDate(millis),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Text(
                text = "›",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun VaccineHistoryCard(
    vaccine: VaccineEntity,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            // Ícone
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.10f,
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "💉",
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp),
            )

            // Informações da vacina
            Column(
                modifier = Modifier.weight(1f),
            ) {

                // Nome
                Text(
                    text = vaccine.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                )

                if (!vaccine.doseDescription.isNullOrBlank()) {
                    Spacer(
                        modifier = Modifier.height(2.dp),
                    )

                    Text(
                        text = vaccine.doseDescription,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                // Data + status
                if (vaccine.applicationDateMillis != null) {

                    Spacer(
                        modifier = Modifier.height(6.dp),
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = formatVaccineDate(
                                vaccine.applicationDateMillis,
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp),
                        )

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.secondary.copy(
                                alpha = 0.14f,
                            ),
                        ) {
                            Text(
                                text = "Aplicada",
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 3.dp,
                                ),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary,
                                fontWeight = FontWeight.Medium,
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.width(6.dp),
            )

            // Seta
            Text(
                text = "›",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
fun EmptyVaccinesContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = PetLifeSpacing.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "💉",
            style = MaterialTheme.typography.headlineLarge,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Small),
        )

        Text(
            text = "Nenhuma vacina cadastrada.",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
        )

        Text(
            text = "Adicione a primeira vacina do seu pet.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun VaccineImportantInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(
                alpha = 0.06f,
            ),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = PetLifeSpacing.Medium,
                    vertical = 12.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.10f,
                        ),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "ⓘ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )
            }

            Spacer(
                modifier = Modifier.width(12.dp),
            )

            Text(
                text = "Importante: Consulte sempre o médico veterinário " +
                        "para definir o protocolo de vacinação ideal para o seu pet.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
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
        DateTimeFormatter.ofPattern("dd/MM/yyyy"),
    )
}