package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.data.domain.Period
import pl.podwikagrzegorz.gardener.data.domain.asListOfBasicGardens
import pl.podwikagrzegorz.gardener.data.realm.*
import timber.log.Timber

class GardenDAO : DAO<BasicGarden> {
    private val realm: Realm
    var listener: OnExecuteTransactionListener? = null

    override fun insertItem(item: BasicGarden) {
        val generatedNewId = generateId()
        item.id = generatedNewId
        val basicGardenRealm = item.asBasicGardenRealm()

        realm.executeTransactionAsync({ bgRealm ->
            val gardenRealm = GardenRealm(generatedNewId, basicGardenRealm)
            bgRealm.insert(gardenRealm)
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun getItemById(id: Long): BasicGarden? =
        realm.where<GardenRealm>().equalTo(ID, id).findFirstAsync().basicGarden?.asBasicGarden()


    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val gardenRealm = bgRealm.where<GardenRealm>().equalTo(ID, id).findFirst()
            gardenRealm?.cascadeDelete()
        }
    }

    override fun updateItem(item: BasicGarden) {
        realm.executeTransactionAsync({ bgRealm ->
            val basicGardenRealm = bgRealm.where<BasicGardenRealm>().equalTo(ID, item.id).findFirst()
            basicGardenRealm?.apply {
                gardenTitle = item.gardenTitle
                phoneNumber = item.phoneNumber
                period = item.period.asPeriodRealm()
                isGarden = item.isGarden
                uniqueSnapshotName = item.uniqueSnapshotName
                latitude = item.latitude
                longitude = item.longitude
            }

        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun getDomainData(): List<BasicGarden> =
        realm.where<BasicGardenRealm>().findAllAsync().asListOfBasicGardens()


    override fun getLiveDomainData(): MutableLiveData<List<BasicGarden>> =
        getDomainData().asLiveList()

    fun getDomainPeriodData(): List<Period> =
        realm.where<PeriodRealm>().findAllAsync().map { it.asPeriod() }


    override fun closeRealm() {
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