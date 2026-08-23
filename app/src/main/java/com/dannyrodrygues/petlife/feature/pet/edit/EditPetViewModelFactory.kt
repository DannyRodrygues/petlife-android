package com.dannyrodrygues.petlife.feature.pet.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository

class EditPetViewModelFactory(
    private val repository: PetRepository,
    private val petId: Long,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {

        if (modelClass.isAssignableFrom(EditPetViewModel::class.java)) {
            return EditPetViewModel(
                repository = repository,
                petId = petId,
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}",
        )
    }
}