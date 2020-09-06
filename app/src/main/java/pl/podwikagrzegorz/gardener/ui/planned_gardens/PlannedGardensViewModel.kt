package pl.podwikagrzegorz.gardener.ui.planned_gardens

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.data.repo.GardenRepository

class PlannedGardensViewModel @ViewModelInject constructor(
    private val gardenRepository: GardenRepository
) : ViewModel() {

    private val _navigateToAddGarden = MutableLiveData<Boolean>()
    val navigateToAddGarden: LiveData<Boolean>
        get() = _navigateToAddGarden

    private val _eventGardenInserted = MutableLiveData<Boolean>()
    val eventGardenInserted: LiveData<Boolean>
        get() = _eventGardenInserted

    fun onNavigate() {
        _navigateToAddGarden.value = true
    }

    fun onNavigateComplete() {
        _navigateToAddGarden.value = false
    }

    fun onShowSuccessSnackbarComplete() {
        _eventGardenInserted.value = false
    }

    fun addBasicGarden(basicGarden: BasicGarden) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenRepository.insert(basicGarden)
            _eventGardenInserted.postValue(true)
        }

    fun deleteGarden(documentId: String) =
        viewModelScope.launch {
            gardenRepository.delete(documentId)
        }

    fun getQuery() =
        gardenRepository.getQuery()

    fun getQuerySortedByName() =
        gardenRepository.getQuerySortedByName()

    fun getQuerySortedByTimestamp() =
        gardenRepository.getQuerySortedByTimestamp()

}