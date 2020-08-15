package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.podwikagrzegorz.gardener.data.daos.MachineDAO
import pl.podwikagrzegorz.gardener.data.daos.OnExecuteTransactionListener
import pl.podwikagrzegorz.gardener.data.domain.Machine

class MachinesChildViewModel : ViewModel(), OnExecuteTransactionListener {
    private val machineDAO = MachineDAO()

    private val _listOfMachines: MutableLiveData<List<Machine>> =
        machineDAO.getLiveDomainData()
    val listOfMachines: LiveData<List<Machine>>
        get() = _listOfMachines

    private val _eventAddMachine = MutableLiveData<Boolean>()
    val eventAddMachine : LiveData<Boolean>
        get() = _eventAddMachine

    private val _errorEditTextEmpty = MutableLiveData<Boolean>()
    val errorEditTextEmpty : LiveData<Boolean>
        get() = _errorEditTextEmpty

    fun onAddMachine(machineName: String, numbOfMachinesAsString: String) {
        if (numbOfMachinesAsString.isNotEmpty()) {
            val machine = Machine(0, machineName, numbOfMachinesAsString.toInt())
            machineDAO.insertItem(machine)
            _eventAddMachine.value = true
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

    fun deleteMachine(id: Long?) {
        id?.let { machineDAO.deleteItem(id) }
    }

    override fun onAsyncTransactionSuccess() {
        fetchFreshData()
    }

    private fun fetchFreshData() {
        _listOfMachines.value = machineDAO.getDomainData()
    }

    override fun onCleared() {
        machineDAO.closeRealm()
        super.onCleared()
    }

    init {
        machineDAO.listener = this
    }
}

/*    fun findMaxValueOf(itemName: String): Int =
        machineDAO.findMaxValueOf(itemName) ?: GardenerApp.MAX_NUMBER_OF_MACHINES*/