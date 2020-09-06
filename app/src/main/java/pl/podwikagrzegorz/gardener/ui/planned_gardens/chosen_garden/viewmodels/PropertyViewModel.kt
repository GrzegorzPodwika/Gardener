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

class PropertyViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val documentId = stateHandle.get<String>(Constants.GARDEN_TITLE)!!


    fun addListOfPickedProperties(listOfItemRealm: List<Item>) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.addListOfPickedProperties(documentId, listOfItemRealm)
        }

/*
    fun updateNumberOfProperties(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperty(noItems, position)
    }
*/

    fun reverseFlagOnProperty(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.reverseFlagOnProperty(documentId, childDocumentId)
        }

    fun deleteItemFromList(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deletePropertyFromList(documentId, childDocumentId)
        }

    fun getTakenPropertiesQuery(): Query =
        gardenComponentsRepository.getTakenPropertiesQuery(documentId)

    fun getTakenPropertiesQuerySortedByTimestamp(): Query =
        gardenComponentsRepository.getTakenPropertiesQuerySortedByTimestamp(documentId)

}