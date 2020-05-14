package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import java.util.*

open class ManHoursRealm(
    var date: Date = Date(),
    var numberOfWorkedHours: Double = 0.0
) : RealmObject()
