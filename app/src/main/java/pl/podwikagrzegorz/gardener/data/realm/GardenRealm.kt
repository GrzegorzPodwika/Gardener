package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class GardenRealm(
    @PrimaryKey var id: Long = 0,
    val basicGarden: BasicGardenRealm,
    val listOfDescriptions: RealmList<String> = RealmList(),
    val listOfNotes: RealmList<String> = RealmList(),
    val listOfTools: RealmList<String> = RealmList(),
    val listOfMachines: RealmList<String> = RealmList(),
    val listOfProperties: RealmList<String> = RealmList(),
    val listOfShopping: RealmList<String> = RealmList(),
    val mapOfWorkedHours: MutableMap<Long, MutableList<ManHoursRealm>> = HashMap(),
    val listOfTakenPicturesPaths: RealmList<String> = RealmList()
) : RealmObject()