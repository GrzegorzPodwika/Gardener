package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.pojo.Property
import pl.podwikagrzegorz.gardener.data.realm.PropertyMapper
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData

class PropertyDAO(override val realm: Realm) : AbstractRealmDAO<Property, PropertyRealm>(realm) {

    override fun insertItem(item: Property) {
        realm.executeTransaction { realm ->
            val propertyRealm = PropertyRealm(generateId(), item.propertyName, item.numberOfProperties)
            realm.insert(propertyRealm)
        }
    }

    override fun getItemById(id: Long): Property? {
        val propertyRealm = realm.where<PropertyRealm>().equalTo(ID, id).findFirst()

        return propertyRealm?.let { PropertyMapper().fromRealm(it) }
    }

    override fun updateItem(item: Property) {
        realm.executeTransactionAsync { bgRealm ->
            val propertyRealm = bgRealm.where<PropertyRealm>().equalTo(ID, item.id).findFirst()
            propertyRealm?.propertyName = item.propertyName
            propertyRealm?.numberOfProperties = item.numberOfProperties
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync { bgRealm ->
            val result = bgRealm.where<PropertyRealm>().equalTo(ID, id).findFirst()

            result?.deleteFromRealm()
        }
    }

    override fun getRealmResults(): RealmResults<PropertyRealm>
        = realm.where<PropertyRealm>().findAll()


    override fun getLiveRealmResults(): MutableLiveData<RealmResults<PropertyRealm>>
        = realm.where<PropertyRealm>().findAllAsync().asLiveData()


    override fun getItemsList(): List<Property> {
        val properties: MutableList<Property> = ArrayList()
        val propertyMapper = PropertyMapper()

        val realmResults = getRealmResults()

        for (note in realmResults) {
            properties.add(propertyMapper.fromRealm(note))
        }
        return properties
    }

    override fun deleteAllItems() {
        realm.executeTransactionAsync { bgRealm ->
            bgRealm.where<PropertyRealm>().findAll().deleteAllFromRealm()
        }
    }

    override fun generateId(): Long {
        val maxValue = realm.where<PropertyRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }
}