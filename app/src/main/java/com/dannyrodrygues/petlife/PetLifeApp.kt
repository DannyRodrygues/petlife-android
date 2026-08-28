package com.dannyrodrygues.petlife

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.dannyrodrygues.petlife.core.navigation.AppNavHost
import com.dannyrodrygues.petlife.core.tenant.TenantProvider
import com.dannyrodrygues.petlife.core.tenant.config.PetLifeDefaultTenant
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig
import com.dannyrodrygues.petlife.ui.theme.PetLifeTheme

@Composable
fun PetLifeApp(
    tenantConfig: TenantConfig = PetLifeDefaultTenant.config,
) {
    TenantProvider(
        tenantConfig = tenantConfig,
    ) {

        PetLifeTheme(
            brandConfig = tenantConfig.brand,
        ) {

            val navController = rememberNavController()

            AppNavHost(
                navController = navController,
            )
        }
    }
}