package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm

class PropertyViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)

    private val _listOfProperties: MutableLiveData<RealmList<ItemRealm>> =
        gardenComponentsDAO.getLiveListOfProperties()
    val listOfProperties : LiveData<RealmList<ItemRealm>>
        get() = _listOfProperties

    fun addListOfPickedProperties(listOfItemRealm: List<ItemRealm>) {
        gardenComponentsDAO.addListOfPickedProperties(listOfItemRealm)
        refreshLiveDataList()
    }

    fun updateNumberOfProperties(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperty(noItems, position)
        refreshLiveDataList()
    }

    fun reverseFlagOnProperty(position: Int) {
        gardenComponentsDAO.reverseFlagOnProperty(position)
        refreshLiveDataList()
    }

    fun deleteItemFromList(id: Long?) {
        id?.let {
            gardenComponentsDAO.deletePropertyFromList(it)
            refreshLiveDataList()
        }
    }

    private fun refreshLiveDataList() {
        _listOfProperties.postValue(_listOfProperties.value)
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