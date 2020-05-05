package pl.podwikagrzegorz.gardener.data.realm

import pl.podwikagrzegorz.gardener.data.pojo.Machine

class MachineMapper : ItemMapper<Machine, MachineRealm> {
    override fun fromRealm(itemRealm: MachineRealm): Machine {
        val machine = Machine()
        machine.id  = itemRealm.id
        machine.machineName = itemRealm.machineName
        machine.numberOfMachines = itemRealm.numberOfMachines

        return machine
    }
}