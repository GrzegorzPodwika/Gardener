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

    fun getListOfMachinesAsRealmResults(): RealmResults<MachineRealm> =
        machineDAO.getRealmResults()

    fun addMachine(machine: MachineRealm) {
        machineDAO.insertItem(machine)
    }

    fun findMaxValueOf(itemName: String): Int =
        machineDAO.findMaxValueOf(itemName) ?: GardenerApp.MAX_NUMBER_OF_MACHINES

    fun deleteMachine(id: Long?) {
        id?.let { machineDAO.deleteItem(id) }
    }

    override fun onCleared() {
        machineDAO.closeRealm()
        super.onCleared()
    }

}
