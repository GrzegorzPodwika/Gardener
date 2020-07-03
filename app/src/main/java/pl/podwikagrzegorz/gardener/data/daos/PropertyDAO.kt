package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.pojo.Property
import pl.podwikagrzegorz.gardener.data.realm.PropertyMapper
import pl.podwikagrzegorz.gardener.data.realm.PropertyModule
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.PropertiesChildViewModel

class PropertyDAO : DAO<PropertyRealm> {
    private val realm: Realm

    override fun insertItem(item: PropertyRealm) {
        val generatedNewId = generateId()

        realm.executeTransactionAsync { bgRealm ->
            item.id = generatedNewId

            bgRealm.insert(item)
        }
    }

    override fun updateItem(item: PropertyRealm) {
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

    override fun getItemById(id: Long): PropertyRealm?
            = realm.where<PropertyRealm>().equalTo(ID, id).findFirst()


    override fun getRealmResults(): RealmResults<PropertyRealm>
        = realm.where<PropertyRealm>().findAllAsync()


    override fun getLiveRealmResults(): MutableLiveData<RealmResults<PropertyRealm>>
        = realm.where<PropertyRealm>().findAllAsync().asLiveData()

    private fun generateId(): Long {
        val maxValue = realm.where<PropertyRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

    override fun closeRealm() {
        realm.close()
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_PROPERTY_NAME)
            .modules(PropertyModule())
            .build()
        realm = Realm.getInstance(realmConfig)
    }

    companion object {
        private const val ID = "id"
        private const val REALM_PROPERTY_NAME = "property.realm"
    }
}