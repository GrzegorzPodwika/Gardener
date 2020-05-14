package pl.podwikagrzegorz.gardener.data.realm

import io.realm.annotations.RealmModule

@RealmModule(classes = [NoteRealm::class])
class NoteModule

@RealmModule(classes = [ToolRealm::class])
class ToolModule

@RealmModule(classes = [MachineRealm::class])
class MachineModule

@RealmModule(classes = [PropertyRealm::class])
class PropertyModule

@RealmModule(classes = [BasicGardenRealm::class, PeriodRealm::class])
class BasicGardenModule

@RealmModule(classes = [GardenRealm::class])
class GardenModule