package com.dannyrodrygues.petlife.core.tenant.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannyrodrygues.petlife.core.tenant.data.CurrentTenantRepository

class TenantViewModelFactory(
    private val repository: CurrentTenantRepository =
        CurrentTenantRepository(),
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        if (
            modelClass.isAssignableFrom(
                TenantViewModel::class.java,
            )
        ) {
            return TenantViewModel(
                repository = repository,
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}",
        )
    }
}