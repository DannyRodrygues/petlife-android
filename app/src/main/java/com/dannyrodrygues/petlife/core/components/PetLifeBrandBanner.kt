package com.dannyrodrygues.petlife.core.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
fun PetLifeBrandBanner(
    modifier: Modifier = Modifier,
) {
    val tenant = LocalTenantConfig.current

    val remoteBannerUrl =
        BrandAssetUrlProvider.getPublicUrl(
            tenant.brand.bannerPath,
        )

    AsyncImage(
        model = remoteBannerUrl
            ?: R.drawable.home_banner,
        contentDescription = stringResource(
            R.string.home_banner_content_description,
            tenant.name,
        ),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1080f / 420f),
        contentScale = ContentScale.FillWidth,
        error = painterResource(
            R.drawable.home_banner,
        ),
    )
}