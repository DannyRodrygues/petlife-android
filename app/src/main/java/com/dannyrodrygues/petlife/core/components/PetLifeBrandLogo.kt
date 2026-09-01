package com.dannyrodrygues.petlife.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.dannyrodrygues.petlife.R
import com.dannyrodrygues.petlife.core.data.remote.BrandAssetUrlProvider
import com.dannyrodrygues.petlife.core.tenant.LocalTenantConfig

@Composable
fun PetLifeBrandLogo(
    modifier: Modifier = Modifier,
) {
    val tenant = LocalTenantConfig.current

    val remoteLogoUrl =
        BrandAssetUrlProvider.getPublicUrl(
            tenant.brand.logoPath,
        )

    AsyncImage(
        model = remoteLogoUrl
            ?: R.drawable.logo_petlife,
        contentDescription = stringResource(
            R.string.brand_logo_content_description,
            tenant.name,
        ),
        modifier = modifier,
        contentScale = ContentScale.Fit,
        error = painterResource(
            R.drawable.logo_petlife,
        ),
    )
}