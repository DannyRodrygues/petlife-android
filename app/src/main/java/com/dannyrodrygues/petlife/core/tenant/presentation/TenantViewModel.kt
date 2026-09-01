package com.dannyrodrygues.petlife.core.tenant.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.core.tenant.config.PetLifeDefaultTenant
import com.dannyrodrygues.petlife.core.tenant.data.TenantRepository
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TenantViewModel(
    private val repository: TenantRepository,
) : ViewModel() {

    private val _tenantConfig =
        MutableStateFlow<TenantConfig>(
            PetLifeDefaultTenant.config,
        )

    val tenantConfig: StateFlow<TenantConfig> =
        _tenantConfig.asStateFlow()

    init {
        loadDefaultTenant()
    }

    private fun loadDefaultTenant() {
        viewModelScope.launch {

            runCatching {
                repository.getTenantByName(
                    name = PetLifeDefaultTenant.config.name,
                )
            }.onSuccess { remoteTenant ->

                _tenantConfig.value = remoteTenant

            }.onFailure {

                /*
                 * Mantém o Tenant local padrão.
                 *
                 * Assim o aplicativo continua funcionando
                 * mesmo sem internet ou caso o Supabase
                 * esteja temporariamente indisponível.
                 */
                _tenantConfig.value =
                    PetLifeDefaultTenant.config
            }
        }
    }
}