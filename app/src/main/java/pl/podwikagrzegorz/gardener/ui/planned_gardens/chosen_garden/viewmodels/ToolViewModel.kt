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

class ToolViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val documentId = stateHandle.get<String>(Constants.GARDEN_TITLE)!!

    fun addListOfPickedTools(listOfPickedTools: List<Item>) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.addListOfPickedTools(documentId, listOfPickedTools)
        }

    fun reverseFlagOnTool(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.reverseFlagOnTool(documentId, childDocumentId)
        }

    fun deleteItemFromList(childDocumentId: String)  =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deleteToolFromList(documentId, childDocumentId)
        }

    fun getTakenToolsQuery(): Query =
        gardenComponentsRepository.getTakenToolsQuery(documentId)

    fun getTakenToolsQuerySortedByTimestamp(): Query =
        gardenComponentsRepository.getTakenToolsQuerySortedByTimestamp(documentId)
}

/*    // deprecated
/*    TODO ZASTANOWIC SIE CZY POTRZEBNE
fun updateNumberOfTools(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperTool(noItems, position)
    }*/

fun updateListOfActiveTools(listOfActiveTools: List<Boolean>) {
        gardenComponentsDAO.updateListOfActiveTools(listOfActiveTools)
        refreshLiveDataList()
    }*/
