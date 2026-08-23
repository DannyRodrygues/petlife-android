package com.dannyrodrygues.petlife.feature.pet.vaccines.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository.VaccineRepository
import kotlinx.coroutines.launch

class AddVaccineViewModel(
    private val repository: VaccineRepository,
) : ViewModel() {

    fun saveVaccine(
        petId: Long,
        name: String,
        doseDescription: String?,
        applicationDateMillis: Long?,
        nextDoseDateMillis: Long?,
        observations: String?,
        onSaved: () -> Unit,
    ) {
        viewModelScope.launch {

            val vaccine = VaccineEntity(
                petId = petId,
                name = name.trim(),
                doseDescription = doseDescription
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() },
                applicationDateMillis = applicationDateMillis,
                nextDoseDateMillis = nextDoseDateMillis,
                observations = observations
                    ?.trim()
                    ?.takeIf { it.isNotEmpty() },
            )

            repository.insertVaccine(vaccine)

            onSaved()
        }
    }
}