package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.ActiveString
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_DOCUMENT_ID

class DescriptionViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val documentId = stateHandle.get<String>(FIREBASE_DOCUMENT_ID)!!

    private val _eventDescriptionAdded = MutableLiveData<Boolean>()
    val eventDescriptionAdded: LiveData<Boolean>
        get() = _eventDescriptionAdded

    private val _errorEmptyInput = MutableLiveData<Boolean>()
    val errorEmptyInput: LiveData<Boolean>
        get() = _errorEmptyInput

    fun onAddDescription(description: String) {
        if (description.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val newDescription = ActiveString(description)
                gardenComponentsRepository.insertDescription(documentId, newDescription)
                _eventDescriptionAdded.postValue(true)
            }
        } else {
            _errorEmptyInput.value = true
        }
    }


    fun onAddDescriptionComplete() {
        _eventDescriptionAdded.value = false
    }

    fun onShowErrorComplete() {
        _errorEmptyInput.value = false
    }

    fun reverseFlagOnDescription(childDocumentId: String, isActive: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.reverseFlagOnDescription(documentId, childDocumentId, isActive)
        }

    fun updateDescription(newDescription: ActiveString) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.updateDescription(documentId, newDescription.documentId, newDescription)
        }

    fun deleteDescriptionFromList(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deleteDescriptionFromList(documentId, childDocumentId)
        }

    fun getDescriptionQuery(): Query =
        gardenComponentsRepository.getDescriptionQuery(documentId)

    fun getDescriptionQuerySortedByTimestamp(): Query =
        gardenComponentsRepository.getDescriptionQuerySortedByTimestamp(documentId)

    fun getDescriptionQuerySortedByActivity(): Query =
        gardenComponentsRepository.getDescriptionQuerySortedByActivity(documentId)

}
