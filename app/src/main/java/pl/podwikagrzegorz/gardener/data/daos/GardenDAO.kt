package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmList
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.realm.*
import pl.podwikagrzegorz.gardener.ui.planned_gardens.PlannedGardensViewModel

class GardenDAO {
    private val realm: Realm

    fun insertItem(basicGardenRealm: BasicGardenRealm) {
        val generatedNewId = generateId()

        realm.executeTransactionAsync { bgRealm ->
            basicGardenRealm.id = generatedNewId

            val gardenRealm = GardenRealm(generatedNewId, basicGardenRealm)
            bgRealm.insert(gardenRealm)

        }
    }

    fun getItemById(id: Long): GardenRealm? =
        realm.where<GardenRealm>().equalTo(ID, id).findFirstAsync()


    fun deleteItem(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val gardenRealm = bgRealm.where<GardenRealm>().equalTo(ID, id).findFirst()
            gardenRealm?.cascadeDelete()
        }
    }

    // BasicGarden
    fun getBasicGardenRealmData(): MutableLiveData<RealmResults<BasicGardenRealm>> =
        realm.where<BasicGardenRealm>().findAllAsync().asLiveData()

    fun getPeriodRealmData(): List<PeriodRealm?> {
        val basicGardens = realm.where<BasicGardenRealm>().findAll()
        val mutableList = ArrayList<PeriodRealm?>()
        for (index in basicGardens.indices){
            mutableList.add(basicGardens[index]?.period)
        }

        return mutableList
    }


    fun closeRealm() {
        realm.close()
    }

    private fun generateId(): Long {
        val maxValue = realm.where<GardenRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_GARDEN_NAME)
            .modules(GardenModule())
            .build()

        realm = Realm.getInstance(realmConfig)
    }

    companion object {
        private const val ID = "id"
        private const val REALM_GARDEN_NAME = "garden.realm"
    }
}