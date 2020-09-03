package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.google.firebase.firestore.Query
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.repo.GardenComponentsRepository
import pl.podwikagrzegorz.gardener.extensions.Constants

class PhotosViewModel @ViewModelInject constructor(
    private val gardenComponentsRepository: GardenComponentsRepository,
    @Assisted private val stateHandle: SavedStateHandle
) : ViewModel() {
    private val documentId = stateHandle.get<String>(Constants.GARDEN_TITLE)!!

    private val _listOfPictureStorageRef = MutableLiveData<List<StorageReference>>()
    val listOfPictureStorageRef: LiveData<List<StorageReference>>
        get() = _listOfPictureStorageRef

    private val _eventOnTakePhoto = MutableLiveData<Boolean>()
    val eventOnTakePhoto: LiveData<Boolean>
        get() = _eventOnTakePhoto

    private val _eventOnPhotoClick = MutableLiveData<Boolean>()
    val eventOnPhotoClick: LiveData<Boolean>
        get() = _eventOnPhotoClick

    fun addPictureToList(absolutePath: String) =
        viewModelScope.launch(Dispatchers.IO) {
            gardenComponentsRepository.insertPictureToList(documentId, absolutePath)
            _listOfPictureStorageRef.postValue(gardenComponentsRepository.getListOfPictureRef(documentId))
        }

    fun onTakePhoto() {
        _eventOnTakePhoto.value = true
    }

    fun onTakePhotoComplete() {
        _eventOnTakePhoto.value = false
    }

    fun onPhotoClick() {
        _eventOnPhotoClick.value = true
    }

    fun onPhotoClickComplete() {
        _eventOnPhotoClick.value = false
    }


    init {
        fetchListOfPictureUrls()
    }

    private fun fetchListOfPictureUrls() =
        viewModelScope.launch {
            _listOfPictureStorageRef.postValue(
                gardenComponentsRepository.getListOfPictureRef(documentId)
            )
        }

    fun getPhotoStorageReference(): StorageReference =
        gardenComponentsRepository.gardenPictureRef

    fun getTakenPhotoQuerySortedByTimestamp(): Query =
        gardenComponentsRepository.getTakenPhotoQuerySortedByTimestamp(documentId)

}