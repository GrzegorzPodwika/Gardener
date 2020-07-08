package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm

//TODO (Zastanowic sie jak wyswietlac te dane Z Tool, Machine itd. i przechowywac)
class ToolViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)

    private val _listOfTools: MutableLiveData<RealmList<ItemRealm>> =
        gardenComponentsDAO.getLiveListOfTools()
    val listOfTools: LiveData<RealmList<ItemRealm>>
        get() = _listOfTools

    fun addListOfPickedTools(listOfItemRealm: List<ItemRealm>) {
        gardenComponentsDAO.addListOfPickedTools(listOfItemRealm)
        refreshLiveDataList()
    }

    fun updateNumberOfTools(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperTool(noItems, position)
        refreshLiveDataList()
    }

    fun updateListOfActiveTools(listOfActiveTools: List<Boolean>) {
        gardenComponentsDAO.updateListOfActiveTools(listOfActiveTools)
        refreshLiveDataList()
    }

    fun changeFlagToOpposite(position: Int) {
        gardenComponentsDAO.changeFlagToOpposite(position)
        refreshLiveDataList()
    }

    fun deleteItemFromList(id: Long?) {
        id?.let {
            gardenComponentsDAO.deleteToolFromList(it)
            refreshLiveDataList()
        }
    }


    private fun refreshLiveDataList() {
        _listOfTools.postValue(_listOfTools.value)
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