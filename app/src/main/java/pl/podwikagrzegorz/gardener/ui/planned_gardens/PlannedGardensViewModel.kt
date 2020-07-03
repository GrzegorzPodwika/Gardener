package pl.podwikagrzegorz.gardener.ui.planned_gardens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.BasicGardenDAO
import pl.podwikagrzegorz.gardener.data.daos.GardenDAO
import pl.podwikagrzegorz.gardener.data.pojo.BasicGarden
import pl.podwikagrzegorz.gardener.data.realm.*

class PlannedGardensViewModel : ViewModel() {
    private val gardenDAO: GardenDAO = GardenDAO()

    private val _listOfBasicGardens : MutableLiveData<RealmResults<BasicGardenRealm>> =
        gardenDAO.getBasicGardenRealmData()
    val listOfBasicGardens : LiveData<RealmResults<BasicGardenRealm>>
        get() = _listOfBasicGardens

    fun addBasicGarden(basicGardenRealm: BasicGardenRealm) {
        gardenDAO.insertItem(basicGardenRealm)
    }

    fun deleteGarden(id: Long?) = id?.let { gardenDAO.deleteItem(id) }

    override fun onCleared() {
        gardenDAO.closeRealm()
        super.onCleared()
    }

}