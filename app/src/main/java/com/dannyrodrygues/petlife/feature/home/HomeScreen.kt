package com.dannyrodrygues.petlife.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dannyrodrygues.petlife.R
import com.dannyrodrygues.petlife.core.data.remote.BrandAssetUrlProvider
import com.dannyrodrygues.petlife.core.tenant.LocalTenantConfig
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.ui.theme.PetLifeSpacing


@Composable
fun HomeScreen(
    pets: List<PetEntity>,
    onAddPetClick: () -> Unit,
    onPetClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tenant = LocalTenantConfig.current
    val remoteBannerUrl =
        BrandAssetUrlProvider.getPublicUrl(
            tenant.brand.bannerPath,
        )
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .navigationBarsPadding(),
        ) {

            /*
             * Banner
             */
            AsyncImage(
                model = remoteBannerUrl
                    ?: R.drawable.home_banner,
                contentDescription = stringResource(
                    R.string.home_banner_content_description,
                    tenant.name,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1080f / 420f),
                contentScale = ContentScale.FillWidth,
                error = painterResource(
                    R.drawable.home_banner,
                ),
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = PetLifeSpacing.Large,
                        vertical = PetLifeSpacing.Large,
                    ),
            ) {

                /*
                 * Saudação
                 */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.home_greeting),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    Spacer(
                        modifier = Modifier.width(PetLifeSpacing.Small),
                    )

                    Text(
                        text = "👋",
                        fontSize = 28.sp,
                    )
                }

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.ExtraSmall),
                )

                Text(
                    text = stringResource(
                        R.string.home_welcome_message,
                        tenant.name,
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.ExtraLarge),
                )

                /*
                 * Título Seus pets
                 */
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.paw_petlife),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(PetLifeSpacing.Large),
                    )

                    Spacer(
                        modifier = Modifier.width(PetLifeSpacing.Small),
                    )

                    Text(
                        text = stringResource(R.string.home_pets_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                Spacer(
                    modifier = Modifier.height(PetLifeSpacing.Small),
                )

                HorizontalDivider(
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.35f,
                    ),
                )

                /*
                 * Conteúdo
                 */
                if (pets.isEmpty()) {

                    EmptyPetsContent(
                        modifier = Modifier.weight(1f),
                    )

                } else {

                    PetsContent(
                        pets = pets,
                        onPetClick = onPetClick,
                        modifier = Modifier.weight(1f),
                    )
                }

                /*
                 * Botão Cadastrar pet
                 */
                Button(
                    onClick = onAddPetClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = MaterialTheme.shapes.large,
                        ),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text(
                        text = "+",
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Spacer(
                        modifier = Modifier.width(PetLifeSpacing.Medium),
                    )

                    Text(
                        text = stringResource(R.string.action_add_pet),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyPetsContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(
                    MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.08f,
                    ),
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.empty_pet),
                contentDescription = null,
                modifier = Modifier.size(76.dp),
                contentScale = ContentScale.Fit,
            )
        }

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.ExtraLarge),
        )

        Text(
            text = stringResource(R.string.home_empty_pets),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )

        Spacer(
            modifier = Modifier.height(PetLifeSpacing.Small),
        )

        Text(
            text = stringResource(
                R.string.home_empty_pets_description,
            ),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun PetsContent(
    pets: List<PetEntity>,
    onPetClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(
                top = PetLifeSpacing.Medium,
                bottom = PetLifeSpacing.Medium,
            ),
        verticalArrangement = Arrangement.spacedBy(
            PetLifeSpacing.Medium,
        ),
    ) {
        pets.forEach { pet ->
            PetCard(
                pet = pet,
                onClick = {
                    onPetClick(pet.id)
                },
            )
        }
    }
}

@Composable
private fun PetCard(
    pet: PetEntity,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PetLifeSpacing.Medium),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            /*
             * Foto do pet
             */
            if (!pet.photoUri.isNullOrBlank()) {
                AsyncImage(
                    model = pet.photoUri,
                    contentDescription = "Foto de ${pet.name}",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = 0.08f,
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(R.drawable.empty_pet),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        contentScale = ContentScale.Fit,
                    )
                }
            }

            Spacer(
                modifier = Modifier.width(PetLifeSpacing.Medium),
            )

            /*
             * Informações
             */
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = pet.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(
                    modifier = Modifier.height(
                        PetLifeSpacing.ExtraSmall,
                    ),
                )

                Text(
                    text = pet.species,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                if (!pet.breed.isNullOrBlank()) {
                    Text(
                        text = pet.breed,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}