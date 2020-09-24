package pl.podwikagrzegorz.gardener.ui.workers

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.data.repo.WorkerRepository

class WorkersViewModel @ViewModelInject constructor(
    private val workerRepository: WorkerRepository
) : ViewModel() {

    private var _listOfWorkers = listOf<Worker>()
    val listOfWorkers: List<Worker>
        get() = _listOfWorkers

    fun addWorkerIntoDatabase(workerName: String) = viewModelScope.launch(Dispatchers.IO) {
        val worker = Worker(workerName)
        workerRepository.insert(worker)
    }

    fun updateWorkerInDatabase(documentId: String, workerToUpdate: Worker) = viewModelScope.launch(Dispatchers.IO) {
        workerRepository.update(documentId, workerToUpdate)
    }

    fun deleteWorker(documentId: String) = viewModelScope.launch(Dispatchers.IO) {
        workerRepository.delete(documentId)
    }

    fun getQuery() =
        workerRepository.getQuery()

    fun getQuerySortedByName() =
        workerRepository.getQuerySortedByName()

    fun getQuerySortedByTimestamp() =
        workerRepository.getQuerySortedByTimestamp()

    fun preInitialize() {}

    init {
        viewModelScope.launch {
            _listOfWorkers = workerRepository.getAllWorkers()
        }
    }
}
