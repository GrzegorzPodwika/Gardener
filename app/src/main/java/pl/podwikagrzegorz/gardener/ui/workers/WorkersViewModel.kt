package pl.podwikagrzegorz.gardener.ui.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.WorkerDAO
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm

class WorkersViewModel : ViewModel() {
    private val workerDAO: WorkerDAO = WorkerDAO()

    private val _listOfWorkers: MutableLiveData<RealmResults<WorkerRealm>>
            = workerDAO.getLiveRealmResults()
    val listOfWorkers: LiveData<RealmResults<WorkerRealm>>
        get() = _listOfWorkers

    fun addWorkerIntoDatabase(name: String, surname: String) {
        val workerRealm = WorkerRealm(0, name, surname)
        workerDAO.insertItem(workerRealm)
    }

    fun deleteWorker(id: Long) {
        workerDAO.deleteItem(id)
    }

    override fun onCleared() {
        workerDAO.closeRealm()
        super.onCleared()
    }
}