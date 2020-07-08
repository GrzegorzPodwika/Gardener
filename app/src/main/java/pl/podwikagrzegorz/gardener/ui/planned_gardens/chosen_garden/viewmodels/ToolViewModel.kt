package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

//TODO (Zastanowic sie jak wyswietlac te dane Z Tool, Machine itd. i przechowywac)
class ToolViewModel(gardenID: Long) : AbstractGardenViewModel(gardenID) {
    private val listOfTools: MutableLiveData<RealmList<ItemRealm>>? =
        gardenRealm?.listOfTools?.asLiveList()

    fun getToolsList() :  LiveData<RealmList<ItemRealm>>? = listOfTools

    fun addToolToList(tool: ItemRealm) {
        realm.executeTransaction {
            gardenRealm?.listOfTools?.add(tool)
        }
        refreshLiveDataList()
    }

    fun updateNumberOfTools(noItems: Int, position: Int){
        realm.executeTransaction{
            gardenRealm?.listOfTools?.get(position)?.numberOfItems = noItems
        }
        refreshLiveDataList()
    }

    override fun deleteItemFromList(id: Long?) {
        if (id != null) {
            realm.executeTransaction {
                gardenRealm?.listOfTools?.removeAt(id.toInt())
            }
            refreshLiveDataList()
        }
    }

    private fun refreshLiveDataList() {
        listOfTools?.postValue(listOfTools.value)
    }

    fun addListOfPickedTools(listOfItemRealm: List<ItemRealm>) {
        realm.executeTransaction {
            gardenRealm?.listOfTools?.addAll(listOfItemRealm)
        }
        refreshLiveDataList()
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