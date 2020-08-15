package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.data.domain.asListOfMachines
import pl.podwikagrzegorz.gardener.data.realm.MachineModule
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveList
import timber.log.Timber

class MachineDAO: DAO<Machine> {
    private val realm: Realm
    var listener: OnExecuteTransactionListener? = null


    override fun insertItem(item: Machine) {
        val generatedNewId = generateId()
        val machineRealm = item.asMachineRealm()

        realm.executeTransactionAsync({ bgRealm ->
            machineRealm.id = generatedNewId
            bgRealm.insert(machineRealm)
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun updateItem(item: Machine) {
        realm.executeTransactionAsync({ bgRealm ->
            val machineRealm = bgRealm.where<MachineRealm>().equalTo(ID, item.id).findFirst()
            machineRealm?.machineName = item.machineName
            machineRealm?.numberOfMachines = item.numberOfMachines
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync({ bgRealm ->
            val machineRealm = bgRealm.where<MachineRealm>().equalTo(ID, id).findFirst()
            machineRealm?.deleteFromRealm()
        }, {
            listener?.onAsyncTransactionSuccess()
        }, { error ->
            Timber.e(error)
        })
    }

    override fun getItemById(id: Long): Machine? =
        realm.where<MachineRealm>().equalTo(ID, id).findFirstAsync()?.asMachine()

    override fun getDomainData(): List<Machine> =
        realm.where<MachineRealm>().findAllAsync().asListOfMachines()

    override fun getLiveDomainData(): MutableLiveData<List<Machine>> =
        getDomainData().asLiveList()

    override fun closeRealm() {
        realm.close()
    }

    private fun generateId(): Long {
        val maxValue = realm.where<MachineRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }

    init {
        val realmConfig = RealmConfiguration.Builder()
            .name(REALM_MACHINE_NAME)
            .modules(MachineModule())
            .build()
        realm = Realm.getInstance(realmConfig)
    }

    companion object {
        private const val ID = "id"
        private const val REALM_MACHINE_NAME = "machine.realm"
    }
}

/*    fun findMaxValueOf(itemName: String): Int? =
        realm.where<MachineRealm>().equalTo("machineName", itemName).findFirst()?.numberOfMachines*/