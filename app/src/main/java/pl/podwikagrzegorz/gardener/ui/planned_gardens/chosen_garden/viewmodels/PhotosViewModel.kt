package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO

class PhotosViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)

    private val _listOfPicturesPaths: MutableLiveData<RealmList<String>> =
        gardenComponentsDAO.getLiveListOfPicturesPaths()
    val listOfPicturesPaths : LiveData<RealmList<String>>
        get() = _listOfPicturesPaths

    fun addPictureToList(path: String) {
        gardenComponentsDAO.addPictureToList(path)
        refreshLiveDataList()
    }

    private fun refreshLiveDataList() {
        _listOfPicturesPaths.postValue(_listOfPicturesPaths.value)
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