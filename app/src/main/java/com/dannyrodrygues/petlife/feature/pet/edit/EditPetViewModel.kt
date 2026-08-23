package com.dannyrodrygues.petlife.feature.pet.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EditPetViewModel(
    private val repository: PetRepository,
    petId: Long,
) : ViewModel() {

    val pet: StateFlow<PetEntity?> =
        repository
            .getPetById(petId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    fun updatePet(
        currentPet: PetEntity,
        name: String,
        species: String,
        breed: String?,
        gender: String,
        birthDate: String?,
        weight: Double?,
        observations: String?,
        photoUri: String?,
        onUpdated: () -> Unit,
    ) {
        viewModelScope.launch {

            val updatedPet = currentPet.copy(
                name = name.trim(),
                species = species,
                breed = breed
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() },
                gender = gender,
                birthDate = birthDate
                    ?.takeIf { it.isNotBlank() },
                weight = weight,
                observations = observations
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() },
                photoUri = photoUri,
            )

            repository.updatePet(updatedPet)

            onUpdated()
        }
    }
}