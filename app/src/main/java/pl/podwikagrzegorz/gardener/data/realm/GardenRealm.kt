package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.extensions.deleteCaptionedImage
import pl.podwikagrzegorz.gardener.extensions.getAbsoluteFilePath

open class GardenRealm(
    @PrimaryKey
    var id: Long = 0,
    var basicGarden: BasicGardenRealm? = BasicGardenRealm(),
    var listOfDescriptions: RealmList<ActiveStringRealm> = RealmList(),
    var listOfNotes: RealmList<ActiveStringRealm> = RealmList(),

    var listOfTools: RealmList<ItemRealm> = RealmList(),
    var listOfMachines: RealmList<ItemRealm> = RealmList(),
    var listOfProperties: RealmList<ItemRealm> = RealmList(),

    var listOfShopping: RealmList<ActiveStringRealm> = RealmList(),
    var mapOfWorkedHours: RealmList<ManHoursMapRealm> = RealmList(),
    var listOfPicturesPaths: RealmList<String> = RealmList()
) : RealmObject() {

    fun cascadeDelete() {
        basicGarden?.cascadeDelete()

        for (activeString in listOfDescriptions) {
            activeString.deleteFromRealm()
        }

        for (activeString in listOfNotes) {
            activeString.deleteFromRealm()
        }

        for (item : ItemRealm in listOfTools){
            item.deleteFromRealm()
        }

        for (item : ItemRealm in listOfMachines){
            item.deleteFromRealm()
        }

        for (item : ItemRealm in listOfProperties){
            item.deleteFromRealm()
        }

        for (activeString in listOfShopping) {
            activeString.deleteFromRealm()
        }

        for (index in mapOfWorkedHours.indices.reversed()){
            val map = mapOfWorkedHours[index]
            map?.cascadeDelete()
        }

        val ctx = GardenerApp.ctx

        // TODO delete already taken pictures
        for (path in listOfPicturesPaths) {
            val absolutePath = ctx.getAbsoluteFilePath(path)
            absolutePath.deleteCaptionedImage()
        }

        deleteFromRealm()
    }
}