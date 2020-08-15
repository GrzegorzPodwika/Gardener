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

class MachineViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID).apply { listener = this@MachineViewModel }

    private val _listOfMachines: MutableLiveData<List<Item>> =
        gardenComponentsDAO.getLiveListOfMachines()
    val listOfMachines: LiveData<List<Item>>
        get() = _listOfMachines

    fun addListOfPickedMachines(listOfPickedMachines: List<ItemRealm>) {
        gardenComponentsDAO.addListOfPickedMachines(listOfPickedMachines)
    }

    fun reverseFlagOnMachine(position: Int) {
        gardenComponentsDAO.reverseFlagOnMachine(position)
    }

    fun updateNumberOfMachines(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperMachine(noItems, position)
    }

    fun deleteItemFromList(id: Long) {
        gardenComponentsDAO.deleteMachineFromList(id)
    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfMachines.value = gardenComponentsDAO.getListOfMachines()
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