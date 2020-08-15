package pl.podwikagrzegorz.gardener.ui.workers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddWorkerViewModel : ViewModel() {

    private val _workerFullName = MutableLiveData<String?>()
    val workerFullName : LiveData<String?>
        get() = _workerFullName

    private val _eventDismiss = MutableLiveData<Boolean>()
    val eventDismiss: LiveData<Boolean>
        get() = _eventDismiss

    fun onAddWorker(fullName: String) {
        _workerFullName.value = fullName
    }

    fun onAddWorkerComplete() {
        _workerFullName.value = null
    }

    fun onDismiss() {
        _eventDismiss.value = true
    }

    fun onDismissComplete() {
        _eventDismiss.value = false
    }
}