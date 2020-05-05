package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MachineRealm (
    @PrimaryKey var id: Long = 0,
    var machineName: String = "",
    var numberOfMachines: Int = 0) : RealmObject()