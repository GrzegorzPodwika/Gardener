package pl.podwikagrzegorz.gardener.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _flowLatitude = MutableLiveData<Double>()
    private val _flowLongitude = MutableLiveData<Double>()
    private val _flowTakenSnapshotName = MutableLiveData<String>()
    val flowLatitude : LiveData<Double> =
        _flowLatitude
    val flowLongitude : LiveData<Double> =
        _flowLongitude
    val flowTakenSnapshotName : LiveData<String> =
        _flowTakenSnapshotName

    fun updateLatitude(latitude: Double) {
        _flowLatitude.value = latitude
    }

    fun updateLongitude(longitude: Double) {
        _flowLongitude.value = longitude
    }

    fun updateTakenSnapshotName(snapshotName: String) {
        _flowTakenSnapshotName.value = snapshotName
    }
}