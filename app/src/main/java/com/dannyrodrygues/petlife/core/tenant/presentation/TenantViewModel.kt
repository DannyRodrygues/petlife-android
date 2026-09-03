package com.dannyrodrygues.petlife.core.tenant.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.core.tenant.config.PetLifeDefaultTenant
import com.dannyrodrygues.petlife.core.tenant.data.CurrentTenantRepository
import com.dannyrodrygues.petlife.core.tenant.model.TenantConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TenantViewModel(
    private val repository: CurrentTenantRepository,
) : ViewModel() {

    private val _tenantConfig =
        MutableStateFlow<TenantConfig>(
            PetLifeDefaultTenant.config,
        )

    val tenantConfig: StateFlow<TenantConfig> =
        _tenantConfig.asStateFlow()

    init {
        loadCurrentTenant()
    }

    fun refreshCurrentTenant() {
        loadCurrentTenant()
    }

    private fun loadCurrentTenant() {
        viewModelScope.launch {
            runCatching {
                repository.getCurrentUserTenant()
            }.onSuccess { tenant ->
                _tenantConfig.value = tenant
            }.onFailure {
                _tenantConfig.value =
                    PetLifeDefaultTenant.config
            }
        }
    }
}