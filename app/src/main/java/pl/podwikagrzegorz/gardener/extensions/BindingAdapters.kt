package pl.podwikagrzegorz.gardener.extensions

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.api.load
import com.google.android.material.card.MaterialCardView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import pl.podwikagrzegorz.gardener.ui.calendar.CalendarFragment
import pl.podwikagrzegorz.gardener.ui.calendar.RangeDecorator
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments.PhotosFragment
import java.io.File
import java.util.*

@BindingAdapter("loadImageViaName")
fun ImageView.loadImageWithName(uniqueName: String?) {
    if (!uniqueName.isNullOrEmpty()) {
        val absolutePath = context.getAbsoluteFilePath(uniqueName)
        load(File(absolutePath))
    }
}

@BindingAdapter("loadIconIfIsGarden")
fun ImageView.loadIconDrawable(isGarden: Boolean) {
    if (isGarden) {
        setImageResource(R.drawable.ic_farm)
    } else {
        setImageResource(R.drawable.ic_lawn_mower)
    }
}

@BindingAdapter("shouldBeCrossed")
fun MaterialCardView.checkIfShouldBeCrossed(isActive: Boolean) {
    foreground = if (isActive) {
        null
    } else {
        GardenerApp.res.getDrawable(R.drawable.stroke_foreground, null)
    }
}

@BindingAdapter("visibility")
fun Button.visibility(numberOfPhotos: Int) {
    isEnabled = numberOfPhotos < PhotosFragment.MAX_NUMBER_OF_POSSIBLE_PICTURES
}

/*
@BindingAdapter("setNameAsGardenOrService")
fun setNameAsGardenOrService(textView: TextView, isChecked: Boolean) {
    textView.apply {
        text = if (isChecked)
            GardenerApp.res.getString(R.string.garden)
        else
            GardenerApp.res.getString(R.string.service)
    }

}*/
