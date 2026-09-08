package com.dannyrodrygues.petlife.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dannyrodrygues.petlife.feature.pet.data.local.PetEntity
import com.dannyrodrygues.petlife.feature.pet.data.repository.PetRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: PetRepository,
) : ViewModel() {

    val pets: StateFlow<List<PetEntity>> =
        repository
            .getAllPets()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList(),
            )

     fun syncPets() {
        viewModelScope.launch {

            /*
             * 1. Envia Pets novos que ainda
             * não possuem remoteId.
             */
            repository.syncUnsyncedPetsToRemote()

            /*
             * 2. Envia edições pendentes.
             */
            repository.syncPendingPetUpdates()

            /*
             * 3. Envia exclusões pendentes.
             */
            repository.syncPendingPetDeletes()

            /*
             * 4. Depois busca registros remotos
             * que ainda não existem localmente.
             */
            repository.syncRemotePetsToLocal()
        }
    }
}