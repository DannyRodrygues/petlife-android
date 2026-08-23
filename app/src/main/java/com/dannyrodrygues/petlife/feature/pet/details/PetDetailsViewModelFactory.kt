package com.dannyrodrygues.petlife.feature.pet.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository

class PetDetailsViewModelFactory(
    private val repository: PetRepository,
    private val petId: Long,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        if (modelClass.isAssignableFrom(PetDetailsViewModel::class.java)) {
            return PetDetailsViewModel(
                repository = repository,
                petId = petId,
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}",
        )
    }
}