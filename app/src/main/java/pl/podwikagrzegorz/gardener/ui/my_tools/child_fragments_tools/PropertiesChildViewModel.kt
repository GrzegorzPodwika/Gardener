package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.PropertyDAO
import pl.podwikagrzegorz.gardener.data.pojo.Property
import pl.podwikagrzegorz.gardener.data.realm.PropertyModule
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.data.realm.propertyDAO

class PropertiesChildViewModel : ViewModel() {
    private val realm: Realm
    private val propertyDAO : PropertyDAO

    fun getPropertyData() : MutableLiveData<RealmResults<PropertyRealm>>
            = propertyDAO.getLiveRealmResults()

    fun addProperty(property : Property){
        propertyDAO.insertItem(property)
    }

    fun deleteProperty(id: Long?){
        id?.let { propertyDAO.deleteItem(id) }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    fun getSingleProperty(id: Long?): Property? = id?.let { propertyDAO.getItemById(id) }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_PROPERTY_NAME)
            .modules(PropertyModule())
            .build()
        realm = Realm.getInstance(realmConfig)
        propertyDAO = realm.propertyDAO()
    }

    companion object{
        const val REALM_PROPERTY_NAME = "property.realm"
    }
}
