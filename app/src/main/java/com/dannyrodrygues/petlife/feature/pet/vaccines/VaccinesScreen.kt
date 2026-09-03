package com.dannyrodrygues.petlife.feature.pet.vaccines

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dannyrodrygues.petlife.R
import com.dannyrodrygues.petlife.core.components.PetLifePrimaryButton
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

@Composable
fun VaccinesScreen(
    pet: PetEntity?,
    vaccines: List<VaccineEntity>,
    onAddVaccineClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .navigationBarsPadding()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
        ) {

            /*
             * Cabeçalho
             */
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 12.dp,
                        vertical = 6.dp,
                    ),
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
                    text = if (pet != null) {
                        "Vacinas da ${pet.name}"
                    } else {
                        "Vacinas"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )

                Icon(
                    painter = painterResource(R.drawable.paw_petlife),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(26.dp),
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = PetLifeSpacing.Medium,
                    ),
            ) {
                if (pet == null) {
                    Text(
                        text = "Carregando informações...",
                        style = MaterialTheme.typography.bodyMedium,
                    )

                    return@Column
                }

                /*
                 * Card informativo
                 */
                VaccineReminderCard(
                    petName = pet.name,
                )

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Large),
                )

                /*
                 * Próxima vacina
                 */
                VaccineSectionTitle(
                    icon = "📅",
                    title = "Próximas vacinas",
                )

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Small),
                )

                val today = LocalDate.now()

                val nextVaccine = vaccines
                    .filter { vaccine ->
                        val nextDose = vaccine.nextDoseDateMillis
                            ?: return@filter false

                        val nextDate = Instant
                            .ofEpochMilli(nextDose)
                            .atZone(ZoneOffset.UTC)
                            .toLocalDate()

                        !nextDate.isBefore(today)
                    }
                    .minByOrNull {
                        it.nextDoseDateMillis ?: Long.MAX_VALUE
                    }

                if (nextVaccine != null) {
                    NextVaccineCard(
                        vaccine = nextVaccine,
                    )
                } else {
                    Text(
                        text = "Nenhuma próxima vacina agendada.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(
                            vertical = PetLifeSpacing.Medium,
                        ),
                    )
                }

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Large),
                )

                /*
                 * Histórico
                 */
                VaccineSectionTitle(
                    icon = "📅",
                    title = "Histórico de vacinas",
                )

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Small),
                )

                if (vaccines.isEmpty()) {
                    EmptyVaccinesContent()
                } else {
                    vaccines
                        .sortedByDescending {
                            it.applicationDateMillis ?: Long.MIN_VALUE
                        }
                        .forEach { vaccine ->

                            VaccineHistoryCard(
                                vaccine = vaccine,
                            )

                            Spacer(
                                modifier = Modifier.height(
                                    PetLifeSpacing.Small,
                                ),
                            )
                        }
                }

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Medium),
                )

                /*
                 * Adicionar vacina
                 */
                PetLifePrimaryButton(
                    text = "+ Adicionar vacina",
                    onClick = onAddVaccineClick,
                    widthFraction = 0.72f,
                )

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Medium),
                )

                VaccineImportantInfoCard()

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Medium),
                )
            }
        }
    }
}