package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.net.Uri
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.*
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.extensions.Constants.GARDEN_TITLE

//TODO Connecting component view models with @GardenComponentRepository
class BasicGardenViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val documentId = stateHandle.get<String>(GARDEN_TITLE)!!

    private val _basicGarden = MutableLiveData<BasicGarden>()
    val basicGarden: LiveData<BasicGarden>
        get() = _basicGarden

    private val _takenMapSnapshotStorageRef = MutableLiveData<StorageReference?>()
    val takenMapSnapshotStorageRef : LiveData<StorageReference?>
        get() = _takenMapSnapshotStorageRef

    private val _eventCallToClient = MutableLiveData<Boolean>()
    val eventCallToClient: LiveData<Boolean>
        get() = _eventCallToClient

    private val _eventNavigateToWorkPlace = MutableLiveData<Boolean>()
    val eventNavigateToWorkPlace: LiveData<Boolean>
        get() = _eventNavigateToWorkPlace

    fun getPhoneNumber(): String = basicGarden.value!!.phoneNumber.toString()

    fun getNavigationIntentUri(): Uri {
        val latitude = basicGarden.value!!.latitude
        val longitude = basicGarden.value!!.longitude

        return Uri.parse("google.navigation:q=$latitude,$longitude")
    }

    fun getTakenSnapshotName(): String {
        return basicGarden.value!!.uniqueSnapshotName
    }

    fun onCall() {
        _eventCallToClient.value = true
    }

    fun onCallComplete() {
        _eventCallToClient.value = false
    }

    fun onNavigate() {

        _eventNavigateToWorkPlace.value = true
    }

    fun onNavigateComplete() {
        _eventNavigateToWorkPlace.value = false
    }

    init {
        initializeBasicGarden()
    }

    private fun initializeBasicGarden() {
        viewModelScope.launch {
            _basicGarden.value = fetchBasicGardenFromRepository()
            _takenMapSnapshotStorageRef.value = fetchMapSnapshotRefFromRepository()
        }
    }

    private suspend fun fetchBasicGardenFromRepository(): BasicGarden {
        return withContext(Dispatchers.IO) {
            gardenComponentsRepository.getBasicGarden(documentId)
        }
    }

    private suspend fun fetchMapSnapshotRefFromRepository(): StorageReference? {
        return withContext(Dispatchers.IO) {
            gardenComponentsRepository.getMapSnapshotStorageRef(documentId)
        }
    }


}