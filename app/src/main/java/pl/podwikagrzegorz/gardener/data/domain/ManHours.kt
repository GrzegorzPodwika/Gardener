package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.ManHoursRealm
import java.util.*

data class ManHours(
    var date: Date = Date(),
    var numberOfWorkedHours: Double = 0.0
) {
    fun asManHoursRealm() : ManHoursRealm =
        ManHoursRealm(date, numberOfWorkedHours)
}