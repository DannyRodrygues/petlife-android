package com.dannyrodrygues.petlife.feature.pet.vaccines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository.VaccineRepository

class VaccinesViewModelFactory(
    private val vaccineRepository: VaccineRepository,
    private val petRepository: PetRepository,
    private val petId: Long,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>,
    ): T {
        if (modelClass.isAssignableFrom(VaccinesViewModel::class.java)) {
            return VaccinesViewModel(
                vaccineRepository = vaccineRepository,
                petRepository = petRepository,
                petId = petId,
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}",
        )
    }
}