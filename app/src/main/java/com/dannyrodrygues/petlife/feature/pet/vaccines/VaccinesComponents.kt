package com.dannyrodrygues.petlife.feature.pet.vaccines

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

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
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val actionWidth = 64.dp
    val totalActionsWidth = actionWidth * 2

    val maxRevealPx = with(LocalDensity.current) {
        totalActionsWidth.toPx()
    }

    var offsetX by remember(vaccine.id) {
        mutableFloatStateOf(0f)
    }

    var showSwipeHint by remember(vaccine.id) {
        mutableStateOf(false)
    }

    /*
     * A dica desaparece automaticamente.
     */
    LaunchedEffect(showSwipeHint) {
        if (showSwipeHint) {
            delay(2_500)
            showSwipeHint = false
        }
    }

    val draggableState = rememberDraggableState { delta ->

        /*
         * Começou a arrastar:
         * esconde a dica.
         */
        showSwipeHint = false

        offsetX = (offsetX + delta)
            .coerceIn(
                minimumValue = -maxRevealPx,
                maximumValue = 0f,
            )
    }

    /*
     * Box externo.
     *
     * Ele não possui clip porque o balão
     * precisa aparecer acima do card.
     */
    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {

        /*
         * Esta Box mantém o card e as ações
         * recortados nas bordas arredondadas.
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
        ) {

            /*
             * Ações escondidas atrás do card.
             */
            Row(
                modifier = Modifier.matchParentSize(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                /*
                 * Editar
                 */
                Box(
                    modifier = Modifier
                        .width(actionWidth)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                        )
                        .clickable {
                            onEditClick()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(
                            android.R.drawable.ic_menu_edit,
                        ),
                        contentDescription = "Editar vacina",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp),
                    )
                }

                /*
                 * Excluir
                 */
                Box(
                    modifier = Modifier
                        .width(actionWidth)
                        .fillMaxHeight()
                        .background(
                            MaterialTheme.colorScheme.errorContainer,
                        )
                        .clickable {
                            onDeleteClick()
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(
                            android.R.drawable.ic_menu_delete,
                        ),
                        contentDescription = "Excluir vacina",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }

            /*
             * Card que desliza.
             */
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset {
                        IntOffset(
                            x = offsetX.roundToInt(),
                            y = 0,
                        )
                    }
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = draggableState,
                        onDragStopped = {
                            offsetX =
                                if (offsetX < -(maxRevealPx / 2f)) {
                                    -maxRevealPx
                                } else {
                                    0f
                                }
                        },
                    )
                    .clickable {

                        /*
                         * Se o card estiver aberto,
                         * o toque apenas fecha.
                         *
                         * Se estiver fechado,
                         * mostra a dica.
                         */
                        if (offsetX < 0f) {
                            offsetX = 0f
                            showSwipeHint = false
                        } else {
                            showSwipeHint = true
                        }
                    },
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

                    /*
                     * Ícone
                     */
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

                    /*
                     * Informações da vacina
                     */
                    Column(
                        modifier = Modifier.weight(1f),
                    ) {

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

                    Text(
                        text = "›",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        /*
         * Balão de ajuda.
         */
        if (showSwipeHint) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .offset(y = (-52).dp)
                    .zIndex(10f),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.inverseSurface,
                contentColor = MaterialTheme.colorScheme.inverseOnSurface,
                tonalElevation = 4.dp,
                shadowElevation = 4.dp,
            ) {
                Text(
                    text = "💡 Arraste para a esquerda para editar ou excluir esta vacina.",
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 8.dp,
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    fontWeight = FontWeight.Medium,
                )
            }
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