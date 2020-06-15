package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmList
import io.realm.RealmObject

open class ManHoursMapRealm(
    var idWorker: Long = 0,
    var listOfManHours: RealmList<ManHoursRealm> = RealmList()
) : RealmObject() {
    fun cascadeDelete() {
        for (index in listOfManHours.indices.reversed()){
            val item = listOfManHours[index]
            item?.deleteFromRealm()
        }
    }
}