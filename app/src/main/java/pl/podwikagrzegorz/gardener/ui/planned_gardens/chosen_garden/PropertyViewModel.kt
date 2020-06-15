package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class PropertyViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {
    private val listOfProperties: MutableLiveData<RealmList<ItemRealm>>? =
        gardenRealm?.listOfProperties?.asLiveList()

    fun getPropertiesList() :  LiveData<RealmList<ItemRealm>>? = listOfProperties

    fun addPropertyToList(property: ItemRealm) {
        realm.executeTransaction {
            gardenRealm?.listOfProperties?.add(property)
            refreshLiveDataList()
        }
    }

    override fun deleteItemFromList(id: Long?) {
        if (id != null) {
            realm.executeTransaction {
                gardenRealm?.listOfProperties?.removeAt(id.toInt())
                refreshLiveDataList()
            }
        }
    }

    private fun refreshLiveDataList() {
        listOfProperties?.postValue(listOfProperties.value)
    }

    fun updateNumberOfProperties(noItems: Int, position: Int) {
        realm.executeTransaction{
            gardenRealm?.listOfProperties?.get(position)?.numberOfItems = noItems
            refreshLiveDataList()
        }
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