package pl.podwikagrzegorz.gardener.extensions



import android.widget.DatePicker
import com.prolificinteractive.materialcalendarview.CalendarDay
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


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

fun Int.convertMonthToName() : String {
    return when(this){
        0 -> "Styczeń"
        1 -> "Luty"
        2 -> "Marzec"
        3 -> "Kwiecień"
        4 -> "Maj"
        5 -> "Czerwiec"
        6 -> "Lipiec"
        7 -> "Sierpień"
        8 -> "Wrzesień"
        9 -> "Październik"
        10 -> "Listopad"
        11 -> "Grudzień"
        else -> throw ArrayIndexOutOfBoundsException("Month out of range : $this")
    }
}

fun Calendar.asCalendarDay() : CalendarDay {
    val year = this.get(Calendar.YEAR)
    val month = this.get(Calendar.MONTH) + 1
    val day = this.get(Calendar.DAY_OF_MONTH)
    return CalendarDay.from(year, month, day)
}

fun Date.toSimpleFormat() : String {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    return simpleDateFormat.format(this)
}

fun DatePicker.getDate() : Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth)
    return calendar.time
}
