package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener

class PhotosViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID).apply { listener = this@PhotosViewModel }

    private val _listOfPicturesPaths: MutableLiveData<List<String>> =
        gardenComponentsDAO.getLiveListOfPicturesPaths()
    val listOfPicturesPaths : LiveData<List<String>>
        get() = _listOfPicturesPaths

    private val _eventOnTakePhoto = MutableLiveData<Boolean>()
    val eventOnTakePhoto : LiveData<Boolean>
        get() = _eventOnTakePhoto

    fun addPictureToList(path: String) {
        gardenComponentsDAO.addPictureToList(path)
    }

    fun onTakePhoto() {
        _eventOnTakePhoto.value = true
    }

    fun onTakePhotoComplete() {
        _eventOnTakePhoto.value = false
    }

    fun onPhotoClick(position: Int){

    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfPicturesPaths.value = gardenComponentsDAO.getListOfPicturesPaths()
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