package pl.podwikagrzegorz.gardener.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.storage.StorageReference
import com.prolificinteractive.materialcalendarview.CalendarDay
import pl.podwikagrzegorz.gardener.MainActivity
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Period
import pl.podwikagrzegorz.gardener.di.GlideApp
import pl.podwikagrzegorz.gardener.extensions.Constants.FIREBASE_DOCUMENT_ID
import pl.podwikagrzegorz.gardener.ui.auth.LoginActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Fragment.toast(msg: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this.requireContext(), msg, duration).show()
}

fun View.snackbar(msg: String, duration: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(this, msg, duration).show()
}

fun ImageView.loadViaReference(storageReference: StorageReference) {
    GlideApp.with(context)
        .load(storageReference)
        .placeholder(R.drawable.ic_place_holder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun String.deleteCaptionedImage() {
    val fileToDelete = File(this)
    if (fileToDelete.exists()) {
        fileToDelete.delete()
    }
}

fun Context.startHomeActivity() =
    Intent(this, MainActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Context.startLoginActivity() =
    Intent(this, LoginActivity::class.java).also {
        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(it)
    }

fun Period.loadRangeIntoPeriod(startDayInMilliseconds: Long, endDayInMilliseconds: Long) {
    val startingDay = Calendar.getInstance()
    startingDay.timeInMillis = startDayInMilliseconds

    val endingDay = Calendar.getInstance()
    endingDay.timeInMillis = endDayInMilliseconds

    apply {
        startDay = startingDay.get(Calendar.DAY_OF_MONTH)
        startMonth =
            startingDay.get(Calendar.MONTH) + 1    // Calendar receives month from range 0-11
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

fun <T> Fragment.getNavigationResult(key: String = "key"): MutableLiveData<T>? =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

fun <T> Fragment.setNavigationResult(key: String = "key", result: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun toBundle(gardenTitle: String) : Bundle {
    val bundle = Bundle()
    bundle.putString(FIREBASE_DOCUMENT_ID, gardenTitle)
    return bundle
}

fun fromBundle(bundle: Bundle) : String {
    return bundle.getString(FIREBASE_DOCUMENT_ID)!!
}