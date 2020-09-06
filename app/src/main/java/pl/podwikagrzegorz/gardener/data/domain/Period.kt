package pl.podwikagrzegorz.gardener.data.domain

import java.io.Serializable

data class Period (
    var startDay: Int = 0,
    var startMonth: Int = 0,
    var startYear: Int = 0,
    var endDay: Int = 0,
    var endMonth: Int = 0,
    var endYear: Int = 0
) : Serializable{

    var periodAsString : String = ""
        get() = String.format(
                "%02d.%02d.%s  -  %02d.%02d.%s",
                startDay,
                startMonth,
                startYear,
                endDay,
                endMonth,
                endYear
            )
}