package pl.podwikagrzegorz.gardener.data.domain

import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.podwikagrzegorz.gardener.data.realm.*

fun RealmResults<NoteRealm>.asListOfNotes(): List<Note> {
    return map { it.asNote() }
}

fun RealmResults<MachineRealm>.asListOfMachines(): List<Machine> {
    return map { it.asMachine() }
}

fun RealmResults<ToolRealm>.asListOfTools(): List<Tool> {
    return map { it.asTool() }
}

fun RealmResults<PropertyRealm>.asListOfProperties(): List<Property> {
    return map { it.asProperty() }
}

fun RealmResults<WorkerRealm>.asListOfWorkers(): List<Worker> {
    return map { it.asWorker() }
}

fun RealmResults<BasicGardenRealm>.asListOfBasicGardens(): List<BasicGarden> {
    return map { it.asBasicGarden() }
}

fun RealmList<ActiveStringRealm>.asListOfActiveStrings(): List<ActiveString> {
    return map { it.asActiveString() }
}

fun RealmList<ItemRealm>.asListOfItems(): List<Item> {
    return map { it.asItem() }
}

fun RealmList<ManHoursMapRealm>.asListOfManHoursMap(): List<ManHoursMap> {
    return map { it.asManHoursMap() }
}

fun RealmList<ManHoursRealm>.asListOfManHours(): List<ManHours> {
    return map { it.asManHours() }
}