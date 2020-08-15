package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.ActiveString

class DescriptionViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO =
        GardenComponentsDAO(gardenID).apply { listener = this@DescriptionViewModel }

    private val _listOfDescriptions: MutableLiveData<List<ActiveString>> =
        gardenComponentsDAO.getLiveListOfDescriptions()
    val listOfDescriptions: LiveData<List<ActiveString>>
        get() = _listOfDescriptions

    private val _eventOnAddedDescription = MutableLiveData<Boolean>()
    val eventOnAddedDescription: LiveData<Boolean>
        get() = _eventOnAddedDescription


    fun onAddDescription(description: String) {
        gardenComponentsDAO.addDescriptionToList(description)
        _eventOnAddedDescription.value = true
    }

    fun onAddDescriptionComplete() {
        _eventOnAddedDescription.value = false
    }

    fun reverseFlagOnDescription(position: Int) {
        gardenComponentsDAO.reverseFlagOnDescription(position)
    }

    fun deleteDescriptionFromList(id: Long) {
        gardenComponentsDAO.deleteDescriptionFromList(id)
    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfDescriptions.value = gardenComponentsDAO.getListOfDescriptions()
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
