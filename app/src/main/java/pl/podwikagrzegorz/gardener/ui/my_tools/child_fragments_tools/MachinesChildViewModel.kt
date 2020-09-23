package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.data.repo.MachineRepository

class MachinesChildViewModel @ViewModelInject constructor(
    private val machineRepository: MachineRepository
) : ViewModel() {

    private var _listOfMachines : List<Machine> = emptyList()
    val listOfMachines : List<Machine>
        get() = _listOfMachines

    private val _listOfMachineNames = mutableListOf<String>()
    val listOfMachineNames : List<String>
        get() = _listOfMachineNames

    private val _eventAddMachine = MutableLiveData<Boolean>()
    val eventAddMachine: LiveData<Boolean>
        get() = _eventAddMachine

    private val _errorEditTextEmpty = MutableLiveData<Boolean>()
    val errorEditTextEmpty: LiveData<Boolean>
        get() = _errorEditTextEmpty

    fun onAddMachine(machineName: String, numbOfMachinesAsString: String) {
        if (machineName.isNotEmpty() && numbOfMachinesAsString.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                val machine = Machine(machineName, numbOfMachinesAsString.toInt())
                machineRepository.insert(machine)
                _eventAddMachine.postValue(true)
            }
        } else {
            onErrorShow()
        }
    }

    private fun onErrorShow() {
        _errorEditTextEmpty.value = true
    }

    fun onAddMachineComplete() {
        _eventAddMachine.value = false
    }

    fun onErrorShowComplete() {
        _errorEditTextEmpty.value = false
    }

    fun updateMachine(documentId: String, newMachineName: String, newNumberOfMachines: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            machineRepository.update(documentId, Machine(newMachineName, newNumberOfMachines))
        }

    fun deleteMachine(documentId: String) =
        viewModelScope.launch(Dispatchers.IO) {
        machineRepository.delete(documentId)
    }

    fun getQuery() =
        machineRepository.getQuery()

    fun getQuerySortedByName() =
        machineRepository.getQuerySortedByName()

    fun getQuerySortedByNumberOfMachines() =
        machineRepository.getQueryByNumberOfMachines()

    fun getQuerySortedByTimestamp() =
        machineRepository.getQuerySortedByTimestamp()

    fun preInitialize() {}

    init {
        fetchListOfMachines()
    }

    private fun fetchListOfMachines() {
        viewModelScope.launch {
             _listOfMachines = machineRepository.getAllMachines()
            for (machine in _listOfMachines) {
                _listOfMachineNames.add(machine.machineName)
            }
        }
    }

}
