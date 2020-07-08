package pl.podwikagrzegorz.gardener.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.GardenDAO
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm

class CalendarViewModel : ViewModel() {
    private val gardenDAO = GardenDAO()
    private val periodsList : List<PeriodRealm>

    fun getListOfPeriods() : List<PeriodRealm> = periodsList

    override fun onCleared() {
        gardenDAO.closeRealm()
        super.onCleared()
    }

    init {
        periodsList = gardenDAO.getPeriodRealmData()
    }
}