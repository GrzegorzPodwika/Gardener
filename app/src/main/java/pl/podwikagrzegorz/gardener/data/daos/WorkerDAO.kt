package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.*

class WorkerDAO : DAO<WorkerRealm> {
    private val realm: Realm

    override fun insertItem(item: WorkerRealm) {
        val generatedNewId = generateId()

        realm.executeTransactionAsync { bgRealm ->
            item.id = generatedNewId

            bgRealm.insert(item)
        }
    }

    override fun updateItem(item: WorkerRealm) {
        realm.executeTransactionAsync { bgRealm ->
            val workerRealm = bgRealm.where<WorkerRealm>().equalTo(ID, item.id).findFirst()
            workerRealm?.name = item.name
            workerRealm?.surname = item.surname
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val result = bgRealm.where<WorkerRealm>().equalTo(ID, id).findFirst()
            result?.deleteFromRealm()
        }
    }

    override fun getItemById(id: Long): WorkerRealm? =
        realm.where<WorkerRealm>().equalTo(ID, id).findFirst()

    override fun getRealmResults(): RealmResults<WorkerRealm> =
        realm.where<WorkerRealm>().findAllAsync()

    override fun getLiveRealmResults(): MutableLiveData<RealmResults<WorkerRealm>> =
        realm.where<WorkerRealm>().findAllAsync().asLiveData()


    override fun closeRealm() {
        realm.close()
    }


    private fun generateId(): Long {
        val maxValue = realm.where<WorkerRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_WORKER_NAME)
            .modules(WorkerModule())
            .build()

        realm = Realm.getInstance(realmConfig)
    }

    companion object {
        private const val ID = "id"
        private const val REALM_WORKER_NAME = "worker.realm"
    }
}