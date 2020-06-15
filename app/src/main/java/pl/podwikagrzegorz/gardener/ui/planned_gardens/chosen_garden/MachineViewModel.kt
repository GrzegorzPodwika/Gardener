package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class MachineViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {
    private val listOfMachines: MutableLiveData<RealmList<ItemRealm>>? =
        gardenRealm?.listOfMachines?.asLiveList()

    fun getMachinesList() :  LiveData<RealmList<ItemRealm>>? = listOfMachines

    fun addMachineToList(machine: ItemRealm) {
        realm.executeTransaction {
            gardenRealm?.listOfMachines?.add(machine)
            refreshLiveDataList()
        }
    }

    override fun deleteItemFromList(id: Long?) {
        if (id != null) {
            realm.executeTransaction {
                gardenRealm?.listOfMachines?.removeAt(id.toInt())
                refreshLiveDataList()
            }
        }
    }

    private fun refreshLiveDataList() {
        listOfMachines?.postValue(listOfMachines.value)
    }

    fun updateNumberOfMachines(noItems: Int, position: Int) {
        realm.executeTransaction{
            gardenRealm?.listOfMachines?.get(position)?.numberOfItems = noItems
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