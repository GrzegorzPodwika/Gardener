package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.ActiveString

class NoteViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO =
        GardenComponentsDAO(gardenID).apply { listener = this@NoteViewModel }

    private val _listOfNotes: MutableLiveData<List<ActiveString>> =
        gardenComponentsDAO.getLiveListOfNotes()
    val listOfNotes: LiveData<List<ActiveString>>
        get() = _listOfNotes

    private val _eventOnAddedNote = MutableLiveData<Boolean>()
    val eventOnAddedNote: LiveData<Boolean>
        get() = _eventOnAddedNote

    fun onAddNote(note: String) {
        gardenComponentsDAO.addNoteToList(note)
        _eventOnAddedNote.value = true
    }

    fun onAddNoteComplete() {
        _eventOnAddedNote.value = false
    }

    fun reverseFlagOnNote(position: Int) {
        gardenComponentsDAO.reverseFlagOnNote(position)
    }

    fun deleteItemFromList(id: Long) {
        gardenComponentsDAO.deleteNoteFromList(id)
    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfNotes.value = gardenComponentsDAO.getListOfNotes()
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