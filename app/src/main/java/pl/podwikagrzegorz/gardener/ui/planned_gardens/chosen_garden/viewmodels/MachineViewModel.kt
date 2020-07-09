package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm

class MachineViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)

    private val _listOfMachines: MutableLiveData<RealmList<ItemRealm>> =
        gardenComponentsDAO.getLiveListOfMachines()
    val listOfMachines: LiveData<RealmList<ItemRealm>>
        get() = _listOfMachines

    fun addListOfPickedMachines(listOfPickedMachines: List<ItemRealm>) {
        gardenComponentsDAO.addListOfPickedMachines(listOfPickedMachines)
        refreshLiveDataList()
    }

    fun reverseFlagOnMachine(position: Int) {
        gardenComponentsDAO.reverseFlagOnMachine(position)
        refreshLiveDataList()
    }

    fun updateNumberOfMachines(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperMachine(noItems, position)
        refreshLiveDataList()
    }

    fun deleteItemFromList(id: Long?) {
        id?.let {
            gardenComponentsDAO.deleteMachineFromList(it)
            refreshLiveDataList()
        }
    }

    private fun refreshLiveDataList() {
        _listOfMachines.postValue(_listOfMachines.value)
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