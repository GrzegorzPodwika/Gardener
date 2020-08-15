package pl.podwikagrzegorz.gardener.data.domain

import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import java.io.Serializable

data class Period (
    var startDay: Int = 0,
    var startMonth: Int = 0,
    var startYear: Int = 0,
    var endDay: Int = 0,
    var endMonth: Int = 0,
    var endYear: Int = 0
) : Serializable{

    val periodAsString : String
        get() = String.format(
                "%02d.%02d.%s  -  %02d.%02d.%s",
                startDay,
                startMonth,
                startYear,
                endDay,
                endMonth,
                endYear
            )

    fun asPeriodRealm() : PeriodRealm =
        PeriodRealm(startDay, startMonth, startYear, endDay, endMonth, endYear)
}