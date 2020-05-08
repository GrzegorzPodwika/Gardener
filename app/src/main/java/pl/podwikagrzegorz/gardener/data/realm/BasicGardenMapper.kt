package pl.podwikagrzegorz.gardener.data.realm

import pl.podwikagrzegorz.gardener.data.pojo.BasicGarden
import pl.podwikagrzegorz.gardener.extensions.mapToPeriod

class BasicGardenMapper : ItemMapper<BasicGarden, BasicGardenRealm> {
    override fun fromRealm(itemRealm: BasicGardenRealm): BasicGarden {
        val garden = BasicGarden()
        garden.id = itemRealm.id
        garden.gardenTitle = itemRealm.gardenTitle
        garden.phoneNumber = itemRealm.phoneNumber
        garden.period = itemRealm.period?.mapToPeriod()
        garden.isGarden = itemRealm.isGarden
        garden.snapshotPath = itemRealm.snapshotPath
        garden.latitude = itemRealm.latitude
        garden.longitude = itemRealm.longitude
        return garden
    }
}