package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.pojo.Machine
import pl.podwikagrzegorz.gardener.data.realm.MachineMapper
import pl.podwikagrzegorz.gardener.data.realm.MachineModule
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData

class MachineDAO : DAO<MachineRealm> {
    private val realm: Realm

    override fun insertItem(item: MachineRealm) {
        val generatedNewId = generateId()

        realm.executeTransactionAsync { bgRealm ->
            item.id = generatedNewId
            bgRealm.insert(item)
        }
    }

    override fun updateItem(item: MachineRealm) {
        realm.executeTransactionAsync {bgRealm ->
            val machineRealm = bgRealm.where<MachineRealm>().equalTo(ID, item.id).findFirst()
            machineRealm?.machineName = item.machineName
            machineRealm?.numberOfMachines = item.numberOfMachines
        }
    }

    override fun deleteItem(id: Long) {
        realm.executeTransactionAsync {bgRealm ->
            val machineRealm = bgRealm.where<MachineRealm>().equalTo(ID, id).findFirst()
            machineRealm?.deleteFromRealm()
        }
    }

    override fun getItemById(id: Long): MachineRealm?
        = realm.where<MachineRealm>().equalTo(ID, id).findFirst()

    override fun getRealmResults(): RealmResults<MachineRealm> {
        return realm.where<MachineRealm>().findAllAsync()
    }

    override fun getLiveRealmResults(): MutableLiveData<RealmResults<MachineRealm>> {
        return realm.where<MachineRealm>().findAllAsync().asLiveData()
    }


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