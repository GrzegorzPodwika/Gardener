package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.ActiveString

class ShoppingViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private val gardenComponentsDAO = GardenComponentsDAO(gardenID).apply { listener = this@ShoppingViewModel }

    private val _listOfShopping: MutableLiveData<List<ActiveString>> =
        gardenComponentsDAO.getLiveListOfShopping()
    val listOfShopping: LiveData<List<ActiveString>>
        get() = _listOfShopping

    private val _eventOnAddedShoppingNote = MutableLiveData<Boolean>()
    val eventOnAddedShoppingNote: LiveData<Boolean>
        get() = _eventOnAddedShoppingNote

    fun onAddShoppingNote(shoppingNote: String) {
        gardenComponentsDAO.addShoppingNoteToList(shoppingNote)
        _eventOnAddedShoppingNote.value = true
    }

    fun onAddShoppingNoteComplete() {
        _eventOnAddedShoppingNote.value = false
    }

    fun reverseFlagOnShoppingNote(position: Int) {
        gardenComponentsDAO.reverseFlagOnShoppingNote(position)
    }

    fun deleteShoppingNoteFromList(id: Long) {
        gardenComponentsDAO.deleteShoppingNoteFromList(id)
    }

    override fun onTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfShopping.value = gardenComponentsDAO.getListOfShopping()
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