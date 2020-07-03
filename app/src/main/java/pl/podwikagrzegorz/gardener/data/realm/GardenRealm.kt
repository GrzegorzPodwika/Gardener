package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class GardenRealm(
    @PrimaryKey
    var id: Long = 0,
    var basicGarden: BasicGardenRealm? = BasicGardenRealm(),
    var listOfDescriptions: RealmList<String> = RealmList(),
    var listOfNotes: RealmList<String> = RealmList(),

    var listOfTools: RealmList<ItemRealm> = RealmList(),
    var listOfMachines: RealmList<ItemRealm> = RealmList(),
    var listOfProperties: RealmList<ItemRealm> = RealmList(),

    var listOfShopping: RealmList<String> = RealmList(),

    var mapOfWorkedHours: RealmList<ManHoursMapRealm> = RealmList(),

    var listOfPicturesPaths: RealmList<String> = RealmList()
) : RealmObject() {

    fun cascadeDelete() {
        basicGarden?.cascadeDelete()

        for (item : ItemRealm in listOfTools){
            item.deleteFromRealm()
        }

        for (item : ItemRealm in listOfMachines){
            item.deleteFromRealm()
        }

        for (item : ItemRealm in listOfProperties){
            item.deleteFromRealm()
        }

        for (index in mapOfWorkedHours.indices.reversed()){
            val map = mapOfWorkedHours[index]
            map?.cascadeDelete()
        }

        deleteFromRealm()
    }
}