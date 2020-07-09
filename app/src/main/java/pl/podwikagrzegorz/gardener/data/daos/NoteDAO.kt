package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.NoteModule
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData

class NoteDAO : DAO<NoteRealm> {
    private val realm: Realm

    override fun insertItem(item: NoteRealm) {
        val generatedNewId = generateId()

        realm.executeTransactionAsync { bgRealm ->
            item.id = generatedNewId

            bgRealm.insert(item)
        }
    }

    override fun updateItem(item: NoteRealm) {
        realm.executeTransactionAsync { bgRealm ->
            val noteRealm = bgRealm.where<NoteRealm>().equalTo(ID, item.id).findFirst()
            noteRealm?.service = item.service
            noteRealm?.priceOfService = item.priceOfService
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val result = bgRealm.where<NoteRealm>().equalTo(ID, id).findFirst()
            result?.deleteFromRealm()
        }
    }

    override fun getItemById(id: Long): NoteRealm? =
        realm.where<NoteRealm>().equalTo(ID, id).findFirst()

    override fun getRealmResults(): RealmResults<NoteRealm>
            = realm.where<NoteRealm>().findAllAsync()

    override fun getLiveRealmResults(): MutableLiveData<RealmResults<NoteRealm>> =
        realm.where<NoteRealm>().findAllAsync().asLiveData()

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