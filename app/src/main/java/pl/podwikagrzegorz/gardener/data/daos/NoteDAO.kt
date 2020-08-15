package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.domain.Note
import pl.podwikagrzegorz.gardener.data.domain.asListOfNotes
import pl.podwikagrzegorz.gardener.data.realm.NoteModule
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList
import timber.log.Timber

class NoteDAO : DAO<Note> {
    private val realm: Realm
    var listener: OnExecuteTransactionListener? = null

    override fun insertItem(item: Note) {
        val generatedNewId = generateId()
        val noteRealm = item.asNoteRealm()

        realm.executeTransactionAsync({ bgRealm ->
            noteRealm.id = generatedNewId
            bgRealm.insert(noteRealm)
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun updateItem(item: Note) {
        realm.executeTransactionAsync({ bgRealm ->
            val noteRealm = bgRealm.where<NoteRealm>().equalTo(ID, item.id).findFirst()
            noteRealm?.service = item.service
            noteRealm?.priceOfService = item.priceOfService
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync({ bgRealm ->
            val result = bgRealm.where<NoteRealm>().equalTo(ID, id).findFirst()
            result?.deleteFromRealm()
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun getItemById(id: Long): Note? =
        realm.where<NoteRealm>().equalTo(ID, id).findFirstAsync()?.asNote()

    override fun getDomainData(): List<Note> =
        realm.where<NoteRealm>().findAllAsync().asListOfNotes()

    override fun getLiveDomainData(): MutableLiveData<List<Note>> =
        getDomainData().asLiveList()

    private fun generateId(): Long {
        val maxValue = realm.where<NoteRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

    override fun closeRealm() {
        realm.close()
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_NOTE_NAME)
            .modules(NoteModule())
            .build()
        realm = Realm.getInstance(realmConfig)
    }

    companion object {
        private const val ID = "id"
        private const val REALM_NOTE_NAME = "note.realm"
    }

}


