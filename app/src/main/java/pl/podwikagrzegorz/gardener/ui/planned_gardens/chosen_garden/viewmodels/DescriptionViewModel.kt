package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.realm.ActiveStringRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList

class DescriptionViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)

    private val _listOfDescriptions: MutableLiveData<RealmList<ActiveStringRealm>> =
        gardenComponentsDAO.getLiveListOfDescriptions()
    val listOfDescriptions: LiveData<RealmList<ActiveStringRealm>>
        get() = _listOfDescriptions


    fun addDescriptionToList(description: String) {
        gardenComponentsDAO.addDescriptionToList(description)

        refreshLiveDataList()
    }

    fun reverseFlagOnDescription(position: Int) {
        gardenComponentsDAO.reverseFlagOnDescription(position)
        refreshLiveDataList()
    }

    fun deleteDescriptionFromList(id: Long?) {
        id?.let {
            gardenComponentsDAO.deleteDescriptionFromList(it)
            refreshLiveDataList()
        }
    }

    private fun refreshLiveDataList() {
        _listOfDescriptions.postValue(_listOfDescriptions.value)
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
