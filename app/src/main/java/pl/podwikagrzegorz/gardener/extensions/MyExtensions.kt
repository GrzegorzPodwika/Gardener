package pl.podwikagrzegorz.gardener.extensions

import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import com.prolificinteractive.materialcalendarview.CalendarDay
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, msg, duration).show()
}

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

fun Period.isDefault() : Boolean {
    return this.startDay == 0 || this.endDay == 0
}

