package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class DescriptionViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {
    private val listOfDescriptions: MutableLiveData<RealmList<String>>? =
        gardenRealm?.listOfDescriptions?.asLiveList()

    override fun getItemsList(): LiveData<RealmList<String>>? =
        listOfDescriptions


    override fun addItemToList(item: String) {
        realm.executeTransaction {
            gardenRealm?.listOfDescriptions?.add(item)
            refreshLiveDataList()
        }
    }

    override fun deleteItemFromList(id: Long?) {
        if (id != null) {
            realm.executeTransaction {
                gardenRealm?.listOfDescriptions?.removeAt(id.toInt())
                refreshLiveDataList()
            }
        }
    }

    private fun refreshLiveDataList() {
        listOfDescriptions?.postValue(listOfDescriptions.value)
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


/*    private val listOfDescriptions: MutableLiveData<RealmList<String>>? =
        gardenRealm?.listOfDescriptions?.asLiveList()

    fun getDescriptionsList(): MutableLiveData<RealmList<String>>? =
        listOfDescriptions

    fun addDescriptionToList(description: String) {
        realm.executeTransaction {
            gardenRealm?.listOfDescriptions?.add(description)
            refreshLiveDataList()
        }
    }

    fun deleteDescription(id: Long?) {
        if (id != null) {
            realm.executeTransaction {
                gardenRealm?.listOfDescriptions?.removeAt(id.toInt())
                refreshLiveDataList()
            }
        }
    }*/