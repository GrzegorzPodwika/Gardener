package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm

data class BasicGarden(
    var id: Long = 0,
    var gardenTitle: String = "",
    var phoneNumber: Int = 0,
    var period: Period = Period(),
    var isGarden: Boolean = true,
    var uniqueSnapshotName: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) {
    fun asBasicGardenRealm() : BasicGardenRealm =
        BasicGardenRealm(id, gardenTitle, phoneNumber, period.asPeriodRealm(), isGarden, uniqueSnapshotName, latitude, longitude)
}