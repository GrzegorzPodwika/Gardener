package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.*

class WorkerDAO {
    private val realm: Realm

    fun addWorker(name: String, surname: String) {
        val generatedId = generateId()

        realm.executeTransactionAsync {bgRealm ->
            val worker = WorkerRealm(generatedId, name, surname)
            bgRealm.insert(worker)
        }
    }

    fun getWorkersList(): MutableLiveData<RealmResults<WorkerRealm>> =
        realm.where<WorkerRealm>().findAllAsync().asLiveData()

    fun getWorkersResults() : RealmResults<WorkerRealm> =
        realm.where<WorkerRealm>().findAll()

    fun closeRealm() {
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

    fun deleteWorkerVia(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val workerRealm = bgRealm.where<WorkerRealm>().equalTo(ID, id).findFirst()
            workerRealm?.deleteFromRealm()
        }
    }


    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_WORKER_NAME)
            .modules(WorkerModule())
            .build()

        realm = Realm.getInstance(realmConfig)
    }

    companion object{
        private const val ID = "id"
        private const val REALM_WORKER_NAME = "worker.realm"
    }
}