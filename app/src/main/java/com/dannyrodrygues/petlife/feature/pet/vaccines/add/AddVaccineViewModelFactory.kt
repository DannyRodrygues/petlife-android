package com.dannyrodrygues.petlife.feature.pet.vaccines.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository.VaccineRepository

class AddVaccineViewModelFactory(
    private val repository: VaccineRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        if (modelClass.isAssignableFrom(AddVaccineViewModel::class.java)) {
            return AddVaccineViewModel(
                repository = repository,
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}",
        )
    }
}