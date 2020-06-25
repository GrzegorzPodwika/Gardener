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
    val listOfWorkers: LiveData<RealmResults<WorkerRealm>>
        get() = _listOfWorkers

    fun addWorkerIntoDatabase(name: String, surname: String) {
        workerDAO.addWorker(name, surname)
    }

    override fun onCleared() {
        workerDAO.closeRealm()
        super.onCleared()
    }

    fun deleteWorker(id: Long) {
        workerDAO.deleteWorkerVia(id)
    }

    init {
        _listOfWorkers = workerDAO.getWorkersList()
    }
}