package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class ShoppingViewModel(gardenID: Long): AbstractGardenViewModel(gardenID) {

    private val listOfShopping: MutableLiveData<RealmList<String>>? =
        gardenRealm?.listOfShopping?.asLiveList()

    override fun getItemsList(): LiveData<RealmList<String>>? =
        listOfShopping


    override fun addItemToList(item: String) {
        realm.executeTransaction {
            gardenRealm?.listOfShopping?.add(item)
            refreshLiveDataList()
        }
    }

    override fun deleteItemFromList(id: Long?) {
        if (id != null) {
            realm.executeTransaction {
                gardenRealm?.listOfShopping?.removeAt(id.toInt())
                refreshLiveDataList()
            }
        }
    }

    private fun refreshLiveDataList() {
        listOfShopping?.postValue(listOfShopping.value)
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