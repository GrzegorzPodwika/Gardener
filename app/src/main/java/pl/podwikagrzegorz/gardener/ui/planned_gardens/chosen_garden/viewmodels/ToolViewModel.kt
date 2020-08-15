package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm

class ToolViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID).apply { listener = this@ToolViewModel }

    private val _listOfTools: MutableLiveData<List<Item>> =
        gardenComponentsDAO.getLiveListOfTools()
    val listOfTools: LiveData<List<Item>>
        get() = _listOfTools

    fun addListOfPickedTools(listOfPickedTools: List<ItemRealm>) {
        gardenComponentsDAO.addListOfPickedTools(listOfPickedTools)
    }

    fun updateNumberOfTools(noItems: Int, position: Int) {
        gardenComponentsDAO.updateNumberOfProperTool(noItems, position)
    }

    fun reverseFlagOnTool(position: Int) {
        gardenComponentsDAO.reverseFlagOnTool(position)
    }

    fun deleteItemFromList(id: Long) {
        gardenComponentsDAO.deleteToolFromList(id)
    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfTools.value = gardenComponentsDAO.getListOfTools()
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

/*    // deprecated
fun updateListOfActiveTools(listOfActiveTools: List<Boolean>) {
        gardenComponentsDAO.updateListOfActiveTools(listOfActiveTools)
        refreshLiveDataList()
    }*/
