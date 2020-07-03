package pl.podwikagrzegorz.gardener.ui.price_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.NoteDAO
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm

open class PriceListViewModel : ViewModel() {
    private val noteDAO = NoteDAO()

    private val _priceList: MutableLiveData<RealmResults<NoteRealm>> = noteDAO.getLiveRealmResults()
    val priceList: LiveData<RealmResults<NoteRealm>>
        get() = _priceList

    fun addNote(note: NoteRealm) {
        noteDAO.insertItem(note)
    }

    fun deleteNote(id: Long?) {
        id?.let { noteDAO.deleteItem(it) }
    }

    override fun onCleared() {
        noteDAO.closeRealm()
        super.onCleared()
    }
}
