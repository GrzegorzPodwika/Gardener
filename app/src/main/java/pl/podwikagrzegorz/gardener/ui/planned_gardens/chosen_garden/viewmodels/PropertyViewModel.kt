package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm

class PropertyViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID).apply { listener = this@PropertyViewModel }

    private val _listOfProperties: MutableLiveData<List<Item>> =
        gardenComponentsDAO.getLiveListOfProperties()
    val listOfProperties: LiveData<List<Item>>
        get() = _listOfProperties

    fun addListOfPickedProperties(listOfItemRealm: List<ItemRealm>) {
        gardenComponentsDAO.addListOfPickedProperties(listOfItemRealm)
    }

    fun updateNumberOfProperties(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperty(noItems, position)
    }

    fun reverseFlagOnProperty(position: Int) {
        gardenComponentsDAO.reverseFlagOnProperty(position)
    }

    fun deleteItemFromList(id: Long) {
        gardenComponentsDAO.deletePropertyFromList(id)
    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfProperties.value = gardenComponentsDAO.getListOfProperties()
    }

    override fun onCleared() {
        gardenComponentsDAO.closeRealm()
        super.onCleared()
    }


    companion object {
        private const val GARDEN_ID = "GARDEN_ID"

        fun toBundle(gardenID: Long): Bundle {
            val bundle = Bundle(1)
            bundle.putLong(GARDEN_ID, gardenID)
            return bundle
        }

        fun fromBundle(bundle: Bundle): Long = bundle.getLong(GARDEN_ID)
    }
}