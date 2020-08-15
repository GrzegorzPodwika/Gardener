package pl.podwikagrzegorz.gardener.data.realm

import io.realm.RealmObject
import pl.podwikagrzegorz.gardener.data.domain.Period


open class PeriodRealm(
    var startDay: Int = 0,
    var startMonth: Int = 0,
    var startYear: Int = 0,
    var endDay: Int = 0,
    var endMonth: Int = 0,
    var endYear: Int = 0
) : RealmObject() {
    fun getPeriodAsString(): String =
        String.format(
            "%02d.%02d.%s  -  %02d.%02d.%s",
            startDay,
            startMonth,
            startYear,
            endDay,
            endMonth,
            endYear
        )

    fun asPeriod() : Period =
        Period(startDay, startMonth, startYear, endDay, endMonth, endYear)
}