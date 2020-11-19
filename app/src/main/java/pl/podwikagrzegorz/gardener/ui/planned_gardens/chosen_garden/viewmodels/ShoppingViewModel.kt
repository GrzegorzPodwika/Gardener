package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.ActiveString
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.extensions.Constants

class ShoppingViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val documentId = stateHandle.get<String>(Constants.FIREBASE_DOCUMENT_ID)!!

    private val _eventShoppingNoteAdded = MutableLiveData<Boolean>()
    val eventShoppingNoteAdded: LiveData<Boolean>
        get() = _eventShoppingNoteAdded

    private val _errorEmptyInput = MutableLiveData<Boolean>()
    val errorEmptyInput: LiveData<Boolean>
        get() = _errorEmptyInput

    fun onAddShoppingNote(shoppingNote: String) {
        if (shoppingNote.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val newShoppingNote = ActiveString(shoppingNote)
                gardenComponentsRepository.insertShoppingNote(documentId, newShoppingNote)
                _eventShoppingNoteAdded.postValue(true)
            }
        } else {
            _errorEmptyInput.value = true
        }
    }

    fun onAddShoppingNoteComplete() {
        _eventShoppingNoteAdded.value = false
    }

    fun onShowErrorComplete() {
        _errorEmptyInput.value = false
    }


    fun reverseFlagOnShoppingNote(childDocumentId: String, isActive: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.reverseFlagOnShoppingNote(documentId, childDocumentId, isActive)
        }

    fun updateShoppingNote(newShoppingNote: ActiveString)  =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.updateShoppingNote(documentId, newShoppingNote.documentId, newShoppingNote)
        }

    fun deleteShoppingNoteFromList(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deleteShoppingNoteFromList(documentId, childDocumentId)
        }

    fun getShoppingNotesQuery(): Query =
        gardenComponentsRepository.getShoppingNotesQuery(documentId)

    fun getShoppingNotesQuerySortedByActivity(): Query =
        gardenComponentsRepository.getShoppingNotesQuerySortedByActivity(documentId)


}