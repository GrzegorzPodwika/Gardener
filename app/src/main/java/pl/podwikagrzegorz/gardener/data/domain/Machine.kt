package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.MachineRealm

data class Machine(
    var id: Long = 0,
    var machineName: String = "",
    var numberOfMachines: Int = 0
) {
    fun asMachineRealm(): MachineRealm =
        MachineRealm(id, machineName, numberOfMachines)
}