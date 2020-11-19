package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.domain.ManHours
import pl.podwikagrzegorz.gardener.data.domain.ManHoursMap
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.data.repo.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.extensions.Constants
import java.util.*

class ManHoursViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel(){
    private val documentId = stateHandle.get<String>(Constants.FIREBASE_DOCUMENT_ID)!!

    private val _mapOfWorkedHours = MutableLiveData<List<ManHoursMap>>()
    val mapOfWorkedHours: LiveData<List<ManHoursMap>>
        get() = _mapOfWorkedHours

    private var _workersFullNames: List<String> = emptyList()
    val workersFullNames: List<String>
        get() = _workersFullNames

    private val listener: OnExecuteTransactionListener = object : OnExecuteTransactionListener {
        override fun onTransactionSuccess() {
            fetchFreshData()
        }
    }

    private fun fetchFreshData() {
        viewModelScope.launch {
            _mapOfWorkedHours.postValue(gardenComponentsRepository.getMapOfManHours(documentId))
            _workersFullNames = getFullNames()
        }
    }


    fun addListOfPickedWorkers(listOfPickedWorkers: List<Worker>) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.addListOfPickedWorkers(documentId, listOfPickedWorkers)
        }

    fun addListOfWorkedHoursWithPickedDate(listOfWorkedHours: List<Double>, date: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            val mapOfManHours = mutableMapOf<String, ManHours>()
            for (index in listOfWorkedHours.indices) {
                if (listOfWorkedHours[index] != 0.0) {
                    mapOfManHours[mapOfWorkedHours.value!![index].worker.documentId] = ManHours(date, listOfWorkedHours[index])
                }
            }
            gardenComponentsRepository.addListOfWorkedHoursWithPickedDate(documentId, mapOfManHours)
        }
    }

    //fun
    fun updateManHours(updatedManHours: ManHours, workerDocumentId: String, manHoursDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.updateNumberOfManHours(documentId, workerDocumentId, manHoursDocumentId, updatedManHours)
        }

    fun deleteManHours(workerDocumentId: String, manHoursDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deleteConcreteManHours(documentId, workerDocumentId, manHoursDocumentId)
        }

    init {
        gardenComponentsRepository.listener = this.listener
        fetchMapOfManHours()
    }

    private fun fetchMapOfManHours() {
        viewModelScope.launch {
            _mapOfWorkedHours.postValue(gardenComponentsRepository.getMapOfManHours(documentId))
            _workersFullNames = getFullNames()
        }
    }

    private suspend fun getFullNames(): List<String> {
        return withContext(Dispatchers.IO) {
            val listOfFullNames = mutableListOf<String>()
            _mapOfWorkedHours.value?.forEach { listOfFullNames.add(it.worker.name) }
            listOfFullNames
        }
    }

    fun deleteParentWorker(parentWorkerDocumentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.deleteParentWorkerWithManHours(documentId, parentWorkerDocumentId)
        }


}