package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.data.domain.asListOfWorkers
import pl.podwikagrzegorz.gardener.data.realm.WorkerModule
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList
import timber.log.Timber

class WorkerDAO : DAO<Worker> {
    private val realm: Realm
    var listener: OnExecuteTransactionListener? = null


    override fun insertItem(item: Worker) {
        val generatedNewId = generateId()

        realm.executeTransactionAsync({ bgRealm ->
            val workerRealm = item.asWorkerRealm()

            workerRealm.id = generatedNewId
            bgRealm.insert(workerRealm)
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun updateItem(item: Worker) {
        realm.executeTransactionAsync({ bgRealm ->
            val workerRealm = bgRealm.where<WorkerRealm>().equalTo(ID, item.id).findFirst()
            workerRealm?.name = item.name
            workerRealm?.surname = item.surname
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync({ bgRealm ->
            val result = bgRealm.where<WorkerRealm>().equalTo(ID, id).findFirst()
            result?.deleteFromRealm()
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun getItemById(id: Long): Worker? =
        realm.where<WorkerRealm>().equalTo(ID, id).findFirstAsync()?.asWorker()

    override fun getDomainData(): List<Worker> =
        realm.where<WorkerRealm>().findAllAsync().asListOfWorkers()

    override fun getLiveDomainData(): MutableLiveData<List<Worker>> =
        getDomainData().asLiveList()


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