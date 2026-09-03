package com.dannyrodrygues.petlife.feature.pet.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetDetailsViewModel(
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

    fun deletePet(
        pet: PetEntity,
        onDeleted: () -> Unit,
    ) {
        viewModelScope.launch {
            repository.deletePet(pet)

            onDeleted()
        }
    }
}