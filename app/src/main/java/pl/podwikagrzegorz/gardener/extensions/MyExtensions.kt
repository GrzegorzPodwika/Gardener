package pl.podwikagrzegorz.gardener.extensions


import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import java.io.File

fun String.deleteCaptionedImage(){
    val fileToDelete = File(this)
    if (fileToDelete.exists()) {
        fileToDelete.delete()
    }
}

fun Period.mapIntoPeriodRealm() : PeriodRealm {
    val periodRealm = PeriodRealm()
    periodRealm.startDay = this.startDay
    periodRealm.startMonth = this.startMonth
    periodRealm.startYear = this.startYear
    periodRealm.endDay = this.endDay
    periodRealm.endMonth = this.endMonth
    periodRealm.endYear = this.endYear
    return periodRealm
}

fun PeriodRealm.mapToPeriod() : Period{
    val period = Period()
    period.startDay = this.startDay
    period.startMonth = this.startMonth
    period.startYear = this.startYear
    period.endDay = this.endDay
    period.endMonth = this.endMonth
    period.endYear = this.endYear

    return period
}