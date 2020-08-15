package pl.podwikagrzegorz.gardener.extensions

import android.content.Context
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.prolificinteractive.materialcalendarview.CalendarDay
import pl.podwikagrzegorz.gardener.data.domain.Period
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

fun Period.loadRangeIntoPeriod(startDayInMilliseconds : Long, endDayInMilliseconds: Long) {
    val startingDay = Calendar.getInstance()
    startingDay.timeInMillis = startDayInMilliseconds

    val endingDay = Calendar.getInstance()
    endingDay.timeInMillis = endDayInMilliseconds

    apply {
        startDay = startingDay.get(Calendar.DAY_OF_MONTH)
        startMonth = startingDay.get(Calendar.MONTH) + 1    // Calendar receives month from range 0-11
        startYear = startingDay.get(Calendar.YEAR)

        endDay = endingDay.get(Calendar.DAY_OF_MONTH)
        endMonth = endingDay.get(Calendar.MONTH) + 1
        endYear = endingDay.get(Calendar.YEAR)
    }
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