package pl.podwikagrzegorz.gardener.ui.planned_gardens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.GardenDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden

class PlannedGardensViewModel : ViewModel(), OnExecuteTransactionListener {
    private val gardenDAO: GardenDAO = GardenDAO().apply { listener = this@PlannedGardensViewModel }

    private val _listOfBasicGardens: MutableLiveData<List<BasicGarden>> =
        gardenDAO.getLiveDomainData()
    val listOfBasicGardens: LiveData<List<BasicGarden>>
        get() = _listOfBasicGardens

    private val _navigateToAddGarden = MutableLiveData<Boolean>()
    val navigateToAddGarden : LiveData<Boolean>
        get() = _navigateToAddGarden

    fun onNavigate() {
        _navigateToAddGarden.value = true
    }

    fun onNavigateComplete() {
        _navigateToAddGarden.value = false
    }

    fun addBasicGarden(basicGarden: BasicGarden) {
        gardenDAO.insertItem(basicGarden)
    }

    fun deleteGarden(id: Long) =
        gardenDAO.deleteItem(id)

    override fun onAsyncTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfBasicGardens.value = gardenDAO.getDomainData()
    }

    override fun onCleared() {
        gardenDAO.closeRealm()
        super.onCleared()
    }

}