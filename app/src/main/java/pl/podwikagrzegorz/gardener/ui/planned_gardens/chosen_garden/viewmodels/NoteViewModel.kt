package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO

class NoteViewModel(gardenID: Long) : ViewModel() {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID)

    private val _listOfNotes: MutableLiveData<RealmList<String>> =
        gardenComponentsDAO.getLiveListOfNotes()
    val listOfNotes : LiveData<RealmList<String>>
        get() = _listOfNotes


    fun addNoteToList(note: String) {
        gardenComponentsDAO.addNoteToList(note)
        refreshLiveDataList()
    }


    fun deleteItemFromList(id: Long?) {
        id?.let {
            gardenComponentsDAO.deleteNoteFromList(it)
            refreshLiveDataList()
        }
    }

    override fun onCleared() {
        gardenComponentsDAO.closeRealm()
        super.onCleared()
    }

    private fun refreshLiveDataList() {
        _listOfNotes.postValue(_listOfNotes.value)
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