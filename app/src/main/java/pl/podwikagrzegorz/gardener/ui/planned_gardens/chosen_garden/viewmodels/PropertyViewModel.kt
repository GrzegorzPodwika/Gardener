package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.ActiveProperty
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.extensions.Constants

class PropertyViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val documentId = stateHandle.get<String>(Constants.FIREBASE_DOCUMENT_ID)!!

    fun addListOfPickedProperties(listOfPickedProperties: List<ActiveProperty>) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.addListOfPickedProperties(documentId, listOfPickedProperties)
        }

    fun reverseFlagOnProperty(childDocumentId: String, isActive: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.reverseFlagOnProperty(documentId, childDocumentId, isActive)
        }

/*    fun updateNumberOfProperties(childDocumentId: String, chosenNumber: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.updateNumberOfProperties(documentId, childDocumentId, chosenNumber)
        }*/

    fun deleteItemFromList(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deletePropertyFromList(documentId, childDocumentId)
        }

    fun getTakenPropertiesQuery(): Query =
        gardenComponentsRepository.getTakenPropertiesQuery(documentId)

    fun getTakenPropertiesQuerySortedByTimestamp(): Query =
        gardenComponentsRepository.getTakenPropertiesQuerySortedByTimestamp(documentId)

    fun getTakenPropertiesQuerySortedByActivity(): Query =
        gardenComponentsRepository.getTakenPropertiesQuerySortedByActivity(documentId)
}