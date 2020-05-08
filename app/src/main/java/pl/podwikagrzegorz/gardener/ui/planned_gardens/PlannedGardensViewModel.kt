package pl.podwikagrzegorz.gardener.ui.planned_gardens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.BasicGardenDAO
import pl.podwikagrzegorz.gardener.data.pojo.BasicGarden
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenModule
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.data.realm.basicGardenDAO

class PlannedGardensViewModel : ViewModel() {
    private val realm: Realm
    private val basicGardenDAO: BasicGardenDAO

    fun getBasicGardenData() : MutableLiveData< RealmResults<BasicGardenRealm> >
        = basicGardenDAO.getLiveRealmResults()

    fun addBasicGarden(basicGarden: BasicGarden){
        basicGardenDAO.insertItem(basicGarden)
    }

    fun deleteBasicGarden(id: Long?){
        id?.let { basicGardenDAO.deleteItem(id) }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_BASIC_GARDEN_NAME)
            .modules(BasicGardenModule())
            .build()

        realm = Realm.getInstance(realmConfig)
        basicGardenDAO = realm.basicGardenDAO()
    }

    companion object{
        private const val REALM_BASIC_GARDEN_NAME = "basicGarden.realm"
    }
}