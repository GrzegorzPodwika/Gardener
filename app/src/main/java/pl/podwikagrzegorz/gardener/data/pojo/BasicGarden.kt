package pl.podwikagrzegorz.gardener.data.pojo

class BasicGarden(
    var id: Long = 0,
    var gardenTitle: String = "",
    var phoneNumber: Int = 0,
    var period: Period? = null,
    var isGarden: Boolean = true,
    var snapshotPath: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)