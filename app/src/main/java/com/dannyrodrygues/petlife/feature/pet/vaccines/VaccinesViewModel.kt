package com.dannyrodrygues.petlife.feature.pet.vaccines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.local.VaccineEntity
import com.dannyrodrygues.petlife.feature.pet.vaccines.data.repository.VaccineRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VaccinesViewModel(
    private val vaccineRepository: VaccineRepository,
    petRepository: PetRepository,
    petId: Long,
) : ViewModel() {

    val pet: StateFlow<PetEntity?> =
        petRepository
            .getPetById(petId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    val vaccines: StateFlow<List<VaccineEntity>> =
        vaccineRepository
            .getVaccinesByPetId(petId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

    fun updateVaccine(
        vaccine: VaccineEntity,
    ) {
        viewModelScope.launch {
            vaccineRepository.updateVaccine(
                vaccine = vaccine,
            )
        }
    }


    fun syncVaccines() {
        viewModelScope.launch {

            vaccineRepository
                .syncUnsyncedVaccinesToRemote()

            vaccineRepository
                .syncPendingVaccineUpdates()

            vaccineRepository
                .syncPendingVaccineDeletes()

            vaccineRepository
                .syncRemoteVaccinesToLocal()
        }
    }

    fun deleteVaccine(
        vaccine: VaccineEntity,
    ) {
        viewModelScope.launch {
            vaccineRepository.deleteVaccine(
                vaccine = vaccine,
            )
        }
    }
}