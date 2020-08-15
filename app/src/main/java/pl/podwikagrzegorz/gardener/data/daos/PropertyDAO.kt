package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.data.domain.asListOfProperties
import pl.podwikagrzegorz.gardener.data.realm.PropertyModule
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList
import timber.log.Timber

class PropertyDAO : DAO<Property> {
    private val realm: Realm
    var listener: OnExecuteTransactionListener? = null

    override fun insertItem(item: Property) {
        val generatedNewId = generateId()
        val propertyRealm = item.asPropertyRealm()

        realm.executeTransactionAsync( { bgRealm ->
            propertyRealm.id = generatedNewId

            bgRealm.insert(propertyRealm)
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun updateItem(item: Property) {
        realm.executeTransactionAsync ({ bgRealm ->
            val propertyRealm = bgRealm.where<PropertyRealm>().equalTo(ID, item.id).findFirst()
            propertyRealm?.propertyName = item.propertyName
            propertyRealm?.numberOfProperties = item.numberOfProperties
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync ({ bgRealm ->
            val result = bgRealm.where<PropertyRealm>().equalTo(ID, id).findFirst()

            result?.deleteFromRealm()
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun getItemById(id: Long): Property?
            = realm.where<PropertyRealm>().equalTo(ID, id).findFirstAsync()?.asProperty()

    override fun getDomainData(): List<Property> =
        realm.where<PropertyRealm>().findAllAsync().asListOfProperties()

    override fun getLiveDomainData(): MutableLiveData<List<Property>> =
        getDomainData().asLiveList()

    fun findMaxValueOf(itemName: String): Int? =
        realm.where<PropertyRealm>().equalTo("propertyName", itemName).findFirst()?.numberOfProperties

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