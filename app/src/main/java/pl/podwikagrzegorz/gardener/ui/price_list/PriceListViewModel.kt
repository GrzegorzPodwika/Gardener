package pl.podwikagrzegorz.gardener.ui.price_list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.NoteDAO
import pl.podwikagrzegorz.gardener.data.realm.noteDAO
import pl.podwikagrzegorz.gardener.data.pojo.Note
import pl.podwikagrzegorz.gardener.data.realm.NoteModule
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm

open class PriceListViewModel : ViewModel() {
    private val realm: Realm
    private val noteDAO :NoteDAO

    fun getNoteData() : MutableLiveData<RealmResults<NoteRealm>> =
        noteDAO.getLiveRealmResults()

    fun addNote(note: Note) {
        noteDAO.insertItem(note)
    }

    fun deleteNote(id: Long?){
        id?.let { noteDAO.deleteItem(it) }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    init {
         val realmConfig = RealmConfiguration.Builder()
            .name(REALM_NOTE_NAME)
            .modules(NoteModule())
            .build()
        realm = Realm.getInstance(realmConfig)
        noteDAO = realm.noteDAO()
    }

    companion object{
        const val REALM_NOTE_NAME = "note.realm"
    }
}
