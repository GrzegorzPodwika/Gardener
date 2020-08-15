package pl.podwikagrzegorz.gardener.ui.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.daos.WorkerDAO
import pl.podwikagrzegorz.gardener.data.domain.Worker

class WorkersViewModel : ViewModel(), OnExecuteTransactionListener {
    private val workerDAO: WorkerDAO = WorkerDAO().apply { listener = this@WorkersViewModel }

    private val _listOfWorkers: MutableLiveData<List<Worker>> =
        workerDAO.getLiveDomainData()
    val listOfWorkers: LiveData<List<Worker>>
        get() = _listOfWorkers

    fun addWorkerIntoDatabase(name: String, surname: String) {
        val worker = Worker(0, name, surname)
        workerDAO.insertItem(worker)
    }

    fun deleteWorker(id: Long) {
        workerDAO.deleteItem(id)
    }

    override fun onAsyncTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfWorkers.value = workerDAO.getDomainData()
    }

    override fun onCleared() {
        workerDAO.closeRealm()
        super.onCleared()
    }

}