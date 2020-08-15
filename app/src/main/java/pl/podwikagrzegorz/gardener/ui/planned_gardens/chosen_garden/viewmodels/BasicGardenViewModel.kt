package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels

import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import pl.podwikagrzegorz.gardener.data.daos.GardenComponentsDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm

//TODO dalsze przerobki,aby nie uzywali ClassRealm,
//2.  uzywanie coroutines
//3. implementacja listenerow
class BasicGardenViewModel(gardenID: Long) : ViewModel(), OnExecuteTransactionListener {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val gardenComponentsDAO = GardenComponentsDAO(gardenID).apply { listener = this@BasicGardenViewModel }

    private val unManagedBasicGardenRealm: BasicGardenRealm =
        gardenComponentsDAO.getBasicGarden()

    private val _basicGarden = MutableLiveData<BasicGarden>()
    val basicGarden: LiveData<BasicGarden>
        get() = _basicGarden

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

    override fun onAsyncTransactionSuccess() {
        TODO("Not yet implemented")
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

    override fun onCleared() {
        gardenComponentsDAO.closeRealm()
        super.onCleared()
        viewModelJob.cancel()
    }

    init {
        initializeBasicGarden()
    }

    private fun initializeBasicGarden() {
        uiScope.launch {
            _basicGarden.value = convertRealmObjectIntoDomain()
        }
    }

    private suspend fun convertRealmObjectIntoDomain(): BasicGarden {
        return withContext(Dispatchers.IO) {
            unManagedBasicGardenRealm.asBasicGarden()
        }
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