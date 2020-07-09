package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.WorkerDAO
import pl.podwikagrzegorz.gardener.data.realm.ManHoursMapRealm
import pl.podwikagrzegorz.gardener.data.realm.ManHoursRealm
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList
import java.util.*

class ManHoursViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)
    private val workerDAO = WorkerDAO()

    private val _mapOfWorkedHours: MutableLiveData<RealmList<ManHoursMapRealm>> =
        gardenComponentsDAO.getLiveMapOfManHours()
    val mapOfWorkedHours: LiveData<RealmList<ManHoursMapRealm>>
        get() = _mapOfWorkedHours

    fun getWorkersFullNames(): List<String>
        = _mapOfWorkedHours.value?.map { it.workerFullName } ?: mutableListOf()

    fun getReceivedWorkers(): RealmResults<WorkerRealm> =
        workerDAO.getRealmResults()

    fun addListOfWorkersFullNames(listOfWorkersFullNames: List<String>) {
        gardenComponentsDAO.addListOfWorkersFullNames(listOfWorkersFullNames)
        refreshLiveDataList()
    }

    fun addListOfWorkedHoursWithPickedDate(listOfWorkedHours: List<Double>, date: Date) {
        gardenComponentsDAO.addListOfWorkedHoursWithPickedDate(listOfWorkedHours, date)
        refreshLiveDataList()
    }

    private fun refreshLiveDataList() {
        _mapOfWorkedHours.postValue(_mapOfWorkedHours.value)
    }

    override fun onCleared() {
        workerDAO.closeRealm()
        gardenComponentsDAO.closeRealm()
        super.onCleared()
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