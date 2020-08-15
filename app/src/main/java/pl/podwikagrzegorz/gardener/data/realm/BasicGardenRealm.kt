package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden

open class BasicGardenRealm(
    var id: Long = 0,
    var gardenTitle: String = "",
    var phoneNumber: Int = 0,
    var period: PeriodRealm? = PeriodRealm(),
    var isGarden: Boolean = true,
    var uniqueSnapshotName: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : RealmObject() {
    fun cascadeDelete() {
        period?.deleteFromRealm()
        deleteFromRealm()
    }

    fun asBasicGarden() : BasicGarden =
        BasicGarden(id, gardenTitle, phoneNumber, period!!.asPeriod(), isGarden, uniqueSnapshotName, latitude, longitude)
}