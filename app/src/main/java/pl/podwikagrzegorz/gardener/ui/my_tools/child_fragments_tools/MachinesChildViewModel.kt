package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.data.daos.MachineDAO
import pl.podwikagrzegorz.gardener.data.pojo.Machine
import pl.podwikagrzegorz.gardener.data.realm.MachineModule
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.data.realm.machineDAO

open class MachinesChildViewModel : ViewModel() {
    private val realm: Realm
    private val machineDAO : MachineDAO

    fun getMachineData() : MutableLiveData<RealmResults<MachineRealm>>
            = machineDAO.getLiveRealmResults()

    fun addMachine(machine : Machine){
        machineDAO.insertItem(machine)
    }

    fun deleteMachine(id: Long?){
        id?.let { machineDAO.deleteItem(id) }
    }

    override fun onCleared() {
        realm.close()
        super.onCleared()
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_MACHINE_NAME)
            .modules(MachineModule())
            .build()
        realm = Realm.getInstance(realmConfig)
        machineDAO = realm.machineDAO()
    }

    companion object{
        const val REALM_MACHINE_NAME = "machine.realm"
    }
}
