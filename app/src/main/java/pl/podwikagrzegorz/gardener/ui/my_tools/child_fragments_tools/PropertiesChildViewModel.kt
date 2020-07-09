package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.daos.PropertyDAO
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm

class PropertiesChildViewModel : ViewModel() {
    private val propertyDAO = PropertyDAO()

    private val _listOfProperties: MutableLiveData<RealmResults<PropertyRealm>> =
        propertyDAO.getLiveRealmResults()
    val listOfProperties: LiveData<RealmResults<PropertyRealm>>
        get() = _listOfProperties

    fun addProperty(property: PropertyRealm) {
        propertyDAO.insertItem(property)
    }

    fun getListOfPropertiesAsRealmResults(): RealmResults<PropertyRealm> =
        propertyDAO.getRealmResults()

    fun findMaxValueOf(itemName: String): Int =
        propertyDAO.findMaxValueOf(itemName) ?: GardenerApp.MAX_NUMBER_OF_MACHINES


    fun deleteProperty(id: Long?) {
        id?.let { propertyDAO.deleteItem(id) }
    }

    override fun onCleared() {
        propertyDAO.closeRealm()
        super.onCleared()
    }

}
