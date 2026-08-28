package com.dannyrodrygues.petlife.core.tenant

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.dannyrodrygues.petlife.core.tenant.config.PetLifeDefaultTenant
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig

val LocalTenantConfig = staticCompositionLocalOf {
    PetLifeDefaultTenant.config
}

@Composable
fun TenantProvider(
    tenantConfig: TenantConfig,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalTenantConfig provides tenantConfig,
        content = content,
    )
}