package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.WorkerDAO
import pl.podwikagrzegorz.gardener.data.realm.ManHoursMapRealm
import pl.podwikagrzegorz.gardener.data.realm.ManHoursRealm
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList
import java.util.*

class ManHoursViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {

    private val workerDAO = WorkerDAO()

    private val _mapOfWorkedHours: MutableLiveData<RealmList<ManHoursMapRealm>>? =
        gardenRealm?.mapOfWorkedHours?.asLiveList()

    val mapOfWorkedHours: LiveData<RealmList<ManHoursMapRealm>>? = _mapOfWorkedHours

    fun getWorkersFullNames(): List<String> {
        val listOfNames = mutableListOf<String>()

        val tmpListOfWorkedHours = _mapOfWorkedHours?.value
        tmpListOfWorkedHours?.let {
            for (item in it) {
                listOfNames.add(item.workerFullName)
            }
        }
        return listOfNames
    }

    fun getWorkersResults(): RealmResults<WorkerRealm> =
        workerDAO.getRealmResults()

    override fun onCleared() {
        workerDAO.closeRealm()
        realm.close()
        super.onCleared()
    }

    fun addListOfWorkersFullName(listOfWorkersFullName: List<String>) {
        realm.executeTransaction {

            for (name in listOfWorkersFullName) {
                val tmpListOfWorkedHours = _mapOfWorkedHours?.value

                tmpListOfWorkedHours?.let { list ->
                    val isCurrentlyInList = list.find { it.workerFullName == name }

                    if (isCurrentlyInList == null)
                        list.add(ManHoursMapRealm(name))
                }

                refreshLiveDataList()
            }

        }
    }

    fun addListOfWorkedHoursWithPickedDate(listOfWorkedHours: List<Double>, date: Date) {
        realm.executeTransaction {
            for (index in listOfWorkedHours.indices) {
                if (listOfWorkedHours[index] != 0.0) {
                    _mapOfWorkedHours?.value?.get(index)?.listOfManHours?.add(
                        ManHoursRealm(
                            date,
                            listOfWorkedHours[index]
                        )
                    )

                }
            }

            refreshLiveDataList()
        }
    }

    private fun refreshLiveDataList() {
        _mapOfWorkedHours?.postValue(_mapOfWorkedHours.value)
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