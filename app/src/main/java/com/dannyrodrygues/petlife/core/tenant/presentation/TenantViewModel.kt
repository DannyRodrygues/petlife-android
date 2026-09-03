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

    /*
     * Tenant realmente resolvido através
     * do usuário autenticado.
     *
     * Este ID será utilizado para acessar
     * dados de negócio no Room.
     */
    private val _resolvedTenantId =
        MutableStateFlow<String?>(null)

    val resolvedTenantId: StateFlow<String?> =
        _resolvedTenantId.asStateFlow()

    init {
        loadCurrentTenant()
    }

    fun refreshCurrentTenant() {
        loadCurrentTenant()
    }

    private fun loadCurrentTenant() {

        /*
         * Enquanto o Tenant está sendo resolvido,
         * nenhum Tenant deve ser considerado
         * autorizado para acessar dados locais.
         */
        _resolvedTenantId.value = null

        viewModelScope.launch {
            runCatching {
                repository.getCurrentUserTenant()

            }.onSuccess { tenant ->

                _tenantConfig.value = tenant

                /*
                 * Somente um Tenant realmente
                 * resolvido pode acessar os dados.
                 */
                _resolvedTenantId.value = tenant.id

            }.onFailure {

                /*
                 * O fallback continua existindo
                 * apenas para manter a interface
                 * visual funcionando.
                 */
                _tenantConfig.value =
                    PetLifeDefaultTenant.config

                /*
                 * Não usamos o Tenant fallback
                 * para acessar Pets/Vacinas.
                 */
                _resolvedTenantId.value = null
            }
        }
    }
}