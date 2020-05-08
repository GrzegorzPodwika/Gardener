package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.pojo.BasicGarden
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenMapper
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData
import pl.podwikagrzegorz.gardener.extensions.*

class BasicGardenDAO(override val realm: Realm) : AbstractRealmDAO<BasicGarden, BasicGardenRealm>(realm){

    override fun insertItem(item: BasicGarden) {
        realm.executeTransaction { realm ->
            val basicGardenRealm = BasicGardenRealm(generateId(), item.gardenTitle, item.phoneNumber,
            item.period?.mapIntoPeriodRealm(), item.isGarden, item.snapshotPath, item.latitude, item.longitude)
            realm.insert(basicGardenRealm)
        }
    }

    override fun getItemById(id: Long): BasicGarden? {
        val basicGardenRealm = realm.where<BasicGardenRealm>().equalTo(ID, id).findFirst()

        return basicGardenRealm?.let { BasicGardenMapper().fromRealm(basicGardenRealm) }
    }

    override fun updateItem(item: BasicGarden) {
        realm.executeTransactionAsync { bgRealm ->
            val basicGardenRealm = bgRealm.where<BasicGardenRealm>().equalTo(ID, item.id).findFirst()
            basicGardenRealm?.gardenTitle = item.gardenTitle
            basicGardenRealm?.phoneNumber = item.phoneNumber
            basicGardenRealm?.period = item.period?.mapIntoPeriodRealm()
            basicGardenRealm?.isGarden = item.isGarden
            basicGardenRealm?.snapshotPath = item.snapshotPath
            basicGardenRealm?.latitude = item.latitude
            basicGardenRealm?.longitude = item.longitude
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val basicGardenRealm = bgRealm.where<BasicGardenRealm>().equalTo(ID, id).findFirst()
            basicGardenRealm?.deleteFromRealm()
        }
    }

    override fun getRealmResults(): RealmResults<BasicGardenRealm>
        = realm.where<BasicGardenRealm>().findAllAsync()

    override fun getLiveRealmResults(): MutableLiveData<RealmResults<BasicGardenRealm>>
        = realm.where<BasicGardenRealm>().findAllAsync().asLiveData()


    override fun getItemsList(): List<BasicGarden> {
        val basicGardens: MutableList<BasicGarden> = ArrayList()
        val gardenMapper = BasicGardenMapper()

        val realmResults = getRealmResults()

        for (note in realmResults) {
            basicGardens.add(gardenMapper.fromRealm(note))
        }
        return basicGardens
    }

    override fun deleteAllItems() {
        realm.executeTransactionAsync { bgRealm ->
            bgRealm.where<BasicGardenRealm>().findAll().deleteAllFromRealm()
        }
    }

    override fun generateId(): Long {
        val maxValue = realm.where<BasicGardenRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

}