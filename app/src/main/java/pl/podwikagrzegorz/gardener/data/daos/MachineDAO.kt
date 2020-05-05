package pl.podwikagrzegorz.gardener.data.daos

import androidx.lifecycle.MutableLiveData
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import pl.podwikagrzegorz.gardener.data.pojo.Machine
import pl.podwikagrzegorz.gardener.data.realm.MachineMapper
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.data.realm.asLiveData

class MachineDAO(override val realm: Realm) : AbstractRealmDAO<Machine, MachineRealm>(realm) {

    override fun insertItem(item: Machine) {
        realm.executeTransaction { realm ->
            val machineRealm = MachineRealm(generateId(), item.machineName, item.numberOfMachines)
            realm.insert(machineRealm)
        }
    }

    override fun getItemById(id: Long): Machine? {
        val machineRealm = realm.where<MachineRealm>().equalTo(ID, id).findFirst()

        return machineRealm?.let { MachineMapper().fromRealm(machineRealm) }
    }

    override fun updateItem(item: Machine) {
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

    override fun getRealmResults(): RealmResults<MachineRealm> {
        return realm.where<MachineRealm>().findAll()
    }

    override fun getLiveRealmResults(): MutableLiveData<RealmResults<MachineRealm>> {
        return realm.where<MachineRealm>().findAllAsync().asLiveData()
    }

    override fun getItemsList(): List<Machine> {
        val notes: MutableList<Machine> = ArrayList()
        val noteMapper = MachineMapper()

        val realmResults = getRealmResults()

        for (note in realmResults) {
            notes.add(noteMapper.fromRealm(note))
        }
        return notes
    }

    override fun deleteAllItems() {
        realm.executeTransactionAsync {bgRealm ->
            bgRealm.where<MachineRealm>().findAll().deleteAllFromRealm()
        }
    }

    override fun generateId(): Long {
        val maxValue = realm.where<MachineRealm>().max(ID)
        var nextId: Long = 0

        if (maxValue != null) {
            nextId = maxValue.toLong() + 1
        }

        return nextId
    }
}