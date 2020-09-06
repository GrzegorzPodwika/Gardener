package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.ActiveString
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.extensions.Constants.GARDEN_TITLE

class NoteViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val documentId = stateHandle.get<String>(GARDEN_TITLE)!!

    private val _eventNoteAdded = MutableLiveData<Boolean>()
    val eventNoteAdded: LiveData<Boolean>
        get() = _eventNoteAdded

    private val _errorEmptyInput = MutableLiveData<Boolean>()
    val errorEmptyInput: LiveData<Boolean>
        get() = _errorEmptyInput

    fun onAddNote(note: String) {
        if (note.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val newNote = ActiveString(note)
                gardenComponentsRepository.insertNote(documentId, newNote)
                _eventNoteAdded.postValue(true)
            }
        } else {
            _errorEmptyInput.value = true
        }
    }

    fun onAddNoteComplete() {
        _eventNoteAdded.value = false
    }

    fun onShowErrorComplete() {
        _errorEmptyInput.value = false
    }


    fun reverseFlagOnNote(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.reverseFlagOnNote(documentId, childDocumentId)
        }

    fun deleteItemFromList(childDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deleteNoteFromList(documentId, childDocumentId)
        }

    fun getNoteQuery() : Query =
        gardenComponentsRepository.getNoteQuery(documentId)

    fun getNoteQuerySortedByTimestamp() : Query =
        gardenComponentsRepository.getNoteQuerySortedByTimestamp(documentId)
}