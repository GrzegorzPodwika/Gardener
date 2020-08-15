package pl.podwikagrzegorz.gardener.data.domain

import io.realm.RealmList
import pl.podwikagrzegorz.gardener.data.realm.ManHoursMapRealm

data class ManHoursMap(
    var workerFullName: String = "",
    var listOfManHours: List<ManHours> = listOf()
) {
/*    fun asManHoursMapRealm() : ManHoursMapRealm =
        ManHoursMapRealm(workerFullName, RealmList(listOfManHours))*/
}