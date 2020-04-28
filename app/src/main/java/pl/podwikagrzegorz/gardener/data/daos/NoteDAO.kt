package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.pojo.Note
import pl.podwikagrzegorz.gardener.data.realm.NoteMapper
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData


//TODO testowanie tego DAO
class NoteDAO(override val realm: Realm) : AbstractRealmDAO<Note, NoteRealm>(realm) {

    override fun insertItem(item: Note) {
        realm.executeTransaction{
            val noteRealm = NoteRealm(generateId(), item.service, item.priceOfService)
            it.insert(noteRealm)
        }
    }

    override fun getItemById(id: Long): Note? {
        val noteRealm =  realm.where<NoteRealm>().equalTo(ID, id).findFirst()

        return noteRealm?.let { NoteMapper().fromRealm(it) }
    }

    override fun updateItem(item: Note) {
        realm.executeTransactionAsync{bgRealm ->
            val noteRealm  = bgRealm.where<NoteRealm>().equalTo(ID, item.id).findFirst()
            noteRealm?.service = item.service
            noteRealm?.priceOfService = item.priceOfService
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync{realm ->
            val result = realm.where<NoteRealm>().equalTo(ID, id).findFirst()
            result?.deleteFromRealm()
        }
    }

    override fun getRealmResults(): RealmResults<NoteRealm> = realm.where<NoteRealm>().findAll()

    override fun getLiveRealmResults(): MutableLiveData<RealmResults<NoteRealm>> =
        realm.where<NoteRealm>().findAllAsync().asLiveData()

    override fun getItemsList(): MutableList<Note> {
        val notes: MutableList<Note> = ArrayList()
        val noteMapper = NoteMapper()

        val realmResults = getRealmResults()

        for (note in realmResults){
            notes.add(noteMapper.fromRealm(note))
        }
        return notes
    }

    override fun deleteAllItems() {
        realm.executeTransactionAsync{realm ->
            realm.where<NoteRealm>().findAll().deleteAllFromRealm()
        }
    }

    override fun generateId(): Long {
        val maxValue = realm.where<NoteRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null){
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

    companion object{
        const val PRIMARY_KEY_FIELD = "primaryKeyField"
    }
}