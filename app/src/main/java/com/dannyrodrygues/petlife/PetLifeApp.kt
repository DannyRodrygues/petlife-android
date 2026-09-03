package com.dannyrodrygues.petlife

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.dannyrodrygues.petlife.core.navigation.AppNavHost
import com.dannyrodrygues.petlife.core.tenant.TenantProvider
import com.dannyrodrygues.petlife.core.tenant.presentation.TenantViewModel
import com.dannyrodrygues.petlife.core.tenant.presentation.TenantViewModelFactory
import com.dannyrodrygues.petlife.ui.theme.PetLifeTheme

@Composable
fun PetLifeApp() {

    val tenantViewModel: TenantViewModel = viewModel(
        factory = TenantViewModelFactory(),
    )

    val tenantConfig by tenantViewModel
        .tenantConfig
        .collectAsState()

    TenantProvider(
        tenantConfig = tenantConfig,
    ) {

        PetLifeTheme(
            brandConfig = tenantConfig.brand,
        ) {

            val navController =
                rememberNavController()

            AppNavHost(
                navController = navController,
                onLoginSuccess = tenantViewModel::refreshCurrentTenant,
            )
        }
    }
}