package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.daos.MachineDAO
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm

open class MachinesChildViewModel : ViewModel() {
    private val machineDAO = MachineDAO()

    private val _listOfMachines: MutableLiveData<RealmResults<MachineRealm>> =
        machineDAO.getLiveRealmResults()
    val listOfMachines: LiveData<RealmResults<MachineRealm>>
        get() = _listOfMachines


    fun addMachine(machine: MachineRealm) {
        machineDAO.insertItem(machine)
    }

    fun getSingleMachine(id: Long?): MachineRealm? =
        id?.let { machineDAO.getItemById(id) }

    fun deleteMachine(id: Long?) {
        id?.let { machineDAO.deleteItem(id) }
    }

    override fun onCleared() {
        machineDAO.closeRealm()
        super.onCleared()
    }

    fun findMaxValueOf(itemName: String): Int {
        val searchedMachine = _listOfMachines.value?.find {
            it.machineName == itemName
        }

        return searchedMachine?.numberOfMachines ?: GardenerApp.MAX_NUMBER_OF_MACHINES
    }

}
