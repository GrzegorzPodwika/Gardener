package pl.podwikagrzegorz.gardener.ui.price_list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.Note
import pl.podwikagrzegorz.gardener.data.repo.PriceListRepository

class PriceListViewModel @ViewModelInject constructor(
    private val priceListRepository: PriceListRepository
) : ViewModel() {

    private val _eventAddNote = MutableLiveData<Boolean>()
    val eventAddNote: LiveData<Boolean>
        get() = _eventAddNote

    private val _errorEmptyInput = MutableLiveData<Boolean>()
    val errorEmptyInput : LiveData<Boolean>
        get() = _errorEmptyInput

    fun onAddNote(service: String, priceOfService: String) {
        if (service.isNotEmpty() && priceOfService.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val newNote = Note(service, priceOfService)
                priceListRepository.insert(newNote)
                _eventAddNote.postValue(true)
            }
        } else {
            _errorEmptyInput.value = true
        }
    }

    fun onAddNoteComplete() {
        _eventAddNote.value = false
    }

    fun onErrorEmptyInputComplete() {
        _errorEmptyInput.value = false
    }

    fun updateNote(updatedNote: Note) = viewModelScope.launch(Dispatchers.IO) {
        priceListRepository.update(updatedNote.documentId, updatedNote)
    }


    fun deleteNote(serviceName: String) = viewModelScope.launch(Dispatchers.IO) {
        priceListRepository.delete(serviceName)
    }

    fun getQuery() =
        priceListRepository.getQuery()

    fun getQuerySortedByName() =
        priceListRepository.getQuerySortedByName()

    fun getQuerySortedByPrice() =
        priceListRepository.getQuerySortedByPrice()

    fun getQuerySortedByTimestamp() =
        priceListRepository.getQuerySortedByTimestamp()


}

/*        private val noteDAO = NoteDAO().apply { listener = this@PriceListViewModel }


override fun onAsyncTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _priceList.value = noteDAO.getDomainData()
    }

    override fun onCleared() {
        noteDAO.closeRealm()
        super.onCleared()
        //viewModelJob.cancel()
    }

    init {
        _eventAddNote.value = false
    }*/
