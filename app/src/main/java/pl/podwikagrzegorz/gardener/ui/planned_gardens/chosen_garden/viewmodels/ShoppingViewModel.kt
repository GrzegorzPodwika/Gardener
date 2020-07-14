package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO

class ShoppingViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)

    private val _listOfShopping: MutableLiveData<RealmList<String>> =
        gardenComponentsDAO.getLiveListOfShopping()
    val listOfShopping: LiveData<RealmList<String>>
        get() = _listOfShopping

    fun addShoppingNoteToList(shoppingNote: String) {
        gardenComponentsDAO.addShoppingNoteToList(shoppingNote)
        refreshLiveDataList()
    }

     fun deleteShoppingNoteFromList(id: Long?) {
        id?.let {
            gardenComponentsDAO.deleteShoppingNoteFromList(it)
            refreshLiveDataList()
        }
    }

    private fun refreshLiveDataList() {
        _listOfShopping.postValue(_listOfShopping.value)
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