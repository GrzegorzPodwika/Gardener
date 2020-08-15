package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.ManHoursMap
import java.util.*

class ManHoursViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID).apply { listener = this@ManHoursViewModel }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _mapOfWorkedHours: MutableLiveData<List<ManHoursMap>> =
        gardenComponentsDAO.getLiveMapOfManHours()
    val mapOfWorkedHours: LiveData<List<ManHoursMap>>
        get() = _mapOfWorkedHours

    private var _workersFullNames : List<String> = emptyList()
    val workersFullNames: List<String>
        get() = _workersFullNames


    fun addListOfWorkersFullNames(listOfWorkersFullNames: List<String>) {
        gardenComponentsDAO.addListOfWorkersFullNames(listOfWorkersFullNames)
    }

    fun addListOfWorkedHoursWithPickedDate(listOfWorkedHours: List<Double>, date: Date) {
        gardenComponentsDAO.addListOfWorkedHoursWithPickedDate(listOfWorkedHours, date)
    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _mapOfWorkedHours.value = gardenComponentsDAO.getMapOfManHours()
        fetchWorkerFullNames()
    }

    override fun onCleared() {
        gardenComponentsDAO.closeRealm()
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        fetchWorkerFullNames()
    }

    private fun fetchWorkerFullNames() {
        uiScope.launch {
            _workersFullNames = getFullNames()
        }
    }

    private suspend fun getFullNames(): List<String> {
        return withContext(Dispatchers.Default) {
            val listOfFullNames = mutableListOf<String>()
            _mapOfWorkedHours.value?.forEach { listOfFullNames.add(it.workerFullName) }
            listOfFullNames
        }
    }


    companion object {
        private const val GARDEN_ID = "GARDEN_ID"

        fun toBundle(gardenID: Long): Bundle {
            val bundle = Bundle(1)
            bundle.putLong(GARDEN_ID, gardenID)
            return bundle
        }

        fun fromBundle(bundle: Bundle): Long = bundle.getLong(GARDEN_ID)
    }


}