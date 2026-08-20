package com.dannyrodrygues.petlife.feature.pet.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository
import kotlinx.coroutines.launch

class AddPetViewModel(
    private val repository: PetRepository,
) : ViewModel() {

    fun savePet(
        name: String,
        species: String,
        breed: String?,
        gender: String,
        birthDate: String?,
        weight: Double?,
        observations: String?,
        photoUri: String?,
        onSaved: () -> Unit,
    ) {
        viewModelScope.launch {
            val pet = PetEntity(
                name = name.trim(),
                species = species,
                breed = breed?.trim()?.takeIf {
                    it.isNotEmpty()
                },
                gender = gender,
                birthDate = birthDate?.takeIf {
                    it.isNotBlank()
                },
                weight = weight,
                observations = observations?.trim()?.takeIf {
                    it.isNotEmpty()
                },
                photoUri = photoUri,
            )

            repository.insertPet(pet)

            onSaved()
        }
    }
}

