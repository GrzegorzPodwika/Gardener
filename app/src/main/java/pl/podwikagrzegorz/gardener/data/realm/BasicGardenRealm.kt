package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class BasicGardenRealm(
    var id: Long = 0,
    var gardenTitle: String = "",
    var phoneNumber: Int = 0,
    var period: PeriodRealm? = PeriodRealm(),
    var isGarden: Boolean = true,
    var snapshotPath: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : RealmObject() {
    fun cascadeDelete() {
        period?.deleteFromRealm()
        deleteFromRealm()
    }
}