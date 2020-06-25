package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.WorkerDAO
import pl.podwikagrzegorz.gardener.data.realm.ManHoursMapRealm
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class ManHoursViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {

    private val _mapOfWorkedHours: MutableLiveData<RealmList<ManHoursMapRealm>>? =
        gardenRealm?.mapOfWorkedHours?.asLiveList()

    val mapOfWorkedHours: LiveData<RealmList<ManHoursMapRealm>>? = _mapOfWorkedHours
    private val workerDAO = WorkerDAO()

    fun getWorkersResults() : RealmResults<WorkerRealm> =
        workerDAO.getWorkersResults()

    override fun onCleared() {
        workerDAO.closeRealm()
        realm.close()
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