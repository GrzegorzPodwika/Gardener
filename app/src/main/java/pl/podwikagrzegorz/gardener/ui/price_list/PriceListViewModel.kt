package pl.podwikagrzegorz.gardener.ui.price_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.NoteDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.Note

class PriceListViewModel : ViewModel(), OnExecuteTransactionListener {
    private val noteDAO = NoteDAO().apply { listener = this@PriceListViewModel }

    private val _priceList: MutableLiveData<List<Note>> =
        noteDAO.getLiveDomainData()
    val priceList: LiveData<List<Note>>
        get() = _priceList

    private val _eventAddNote = MutableLiveData<Boolean>()
    val eventAddNote : LiveData<Boolean>
        get() = _eventAddNote

    fun onAddNote(service: String, priceOfService: String) {
        val newNote = Note(0, service, priceOfService)
        noteDAO.insertItem(newNote)
        _eventAddNote.value = true
    }

    fun onAddNoteComplete() {
        _eventAddNote.value = false
    }

    fun deleteNote(id: Long) {
        noteDAO.deleteItem(id)
    }

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
    }
}
