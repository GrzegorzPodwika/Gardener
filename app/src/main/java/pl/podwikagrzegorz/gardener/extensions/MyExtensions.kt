package pl.podwikagrzegorz.gardener.extensions

import android.content.Context
import android.graphics.Bitmap
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context.toast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun String.deleteCaptionedImage() {
    val fileToDelete = File(this)
    if (fileToDelete.exists()) {
        fileToDelete.delete()
    }
}

fun Period.mapIntoPeriodRealm(): PeriodRealm {
    val periodRealm = PeriodRealm()
    periodRealm.startDay = this.startDay
    periodRealm.startMonth = this.startMonth
    periodRealm.startYear = this.startYear
    periodRealm.endDay = this.endDay
    periodRealm.endMonth = this.endMonth
    periodRealm.endYear = this.endYear
    return periodRealm
}

fun PeriodRealm.mapToPeriod(): Period {
    val period = Period()
    period.startDay = this.startDay
    period.startMonth = this.startMonth
    period.startYear = this.startYear
    period.endDay = this.endDay
    period.endMonth = this.endMonth
    period.endYear = this.endYear

    return period
}


fun Calendar.asCalendarDay(): CalendarDay {
    val year = this.get(Calendar.YEAR)
    val month = this.get(Calendar.MONTH) + 1
    val day = this.get(Calendar.DAY_OF_MONTH)
    return CalendarDay.from(year, month, day)
}

fun Date.toSimpleFormat(): String {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    return simpleDateFormat.format(this)
}

fun DatePicker.getDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month, dayOfMonth)
    return calendar.time
}

fun Period.isDefault(): Boolean {
    return this.startDay == 0 || this.endDay == 0
}

fun Context.getFileProvider(fileName: String): File =
    File(this.filesDir, fileName)

fun Context.getAbsoluteFilePath(fileName: String): String =
    File(this.filesDir, fileName).absolutePath

fun <T> Fragment.getNavigationResult(key: String = "key") : MutableLiveData<T>? =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(key: String = "key", result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}