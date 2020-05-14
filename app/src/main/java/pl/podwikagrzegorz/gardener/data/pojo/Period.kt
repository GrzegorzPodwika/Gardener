package pl.podwikagrzegorz.gardener.data.pojo

import java.io.Serializable

class Period (
    var startDay: Int = 0,
    var startMonth: Int = 0,
    var startYear: Int = 0,
    var endDay: Int = 0,
    var endMonth: Int = 0,
    var endYear: Int = 0
) : Serializable{

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
}