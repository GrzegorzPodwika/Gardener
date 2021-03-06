package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.extensions.Constants

class MachineViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val documentId = stateHandle.get<String>(Constants.FIREBASE_DOCUMENT_ID)!!

    fun addListOfPickedMachines(listOfPickedMachines: List<Item>) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.addListOfPickedMachines(documentId, listOfPickedMachines)
        }

    fun reverseFlagOnMachine(childDocumentId: String, isActive: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.reverseFlagOnMachine(documentId, childDocumentId, isActive)
        }

    fun updateNumberOfMachines(childDocumentId: String, chosenNumber: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.updateNumberOfMachines(documentId, childDocumentId, chosenNumber)
        }

    fun deleteItemFromList(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deleteMachineFromList(documentId, childDocumentId)
        }

    fun getTakenMachinesQuery(): Query =
        gardenComponentsRepository.getTakenMachinesQuery(documentId)

    fun getTakenMachinesQuerySortedByTimestamp(): Query =
        gardenComponentsRepository.getTakenMachinesQuerySortedByTimestamp(documentId)

    fun getTakenMachinesQuerySortedByActivity(): Query =
        gardenComponentsRepository.getTakenMachinesQuerySortedByActivity(documentId)
}