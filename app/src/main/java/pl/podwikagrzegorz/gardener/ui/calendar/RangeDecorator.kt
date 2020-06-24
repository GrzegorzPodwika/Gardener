package pl.podwikagrzegorz.gardener.ui.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class RangeDecorator(
    private val drawable: Drawable?,
    datesCollection: Collection<CalendarDay>
) : DayViewDecorator{
    private val dates: HashSet<CalendarDay> = HashSet(datesCollection)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(ForegroundColorSpan(Color.WHITE))
        drawable?.let { view?.setSelectionDrawable(it) }
    }
}