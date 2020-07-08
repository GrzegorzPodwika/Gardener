package pl.podwikagrzegorz.gardener.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentCalendarBinding
import pl.podwikagrzegorz.gardener.extensions.asCalendarDay
import java.util.*

class CalendarFragment : Fragment() {

    private val calendarViewModel: CalendarViewModel by lazy {
        ViewModelProvider(this).get(CalendarViewModel::class.java)
    }
    private lateinit var calendarBinding: FragmentCalendarBinding
    private lateinit var calendarView: MaterialCalendarView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        calendarBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false)
        calendarView = calendarBinding.calendar
        return calendarBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        colorPeriodsOfWork()
    }

    private fun colorPeriodsOfWork() {
        val periodsList: List<PeriodRealm> = calendarViewModel.getListOfPeriods()
        val coloredCircle = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.calendar_circle
        )

        val firstDay = Calendar.getInstance()
        val lastDay = Calendar.getInstance()

        for (period in periodsList) {
            firstDay.set(period.startYear, period.startMonth - 1, period.startDay)
            lastDay.set(period.endYear, period.endMonth - 1, period.endDay)
            val datesSet = getRangeOfDates(firstDay, lastDay)

            calendarView.addDecorator(
                RangeDecorator(
                    coloredCircle,
                    datesSet
                )
            )

        }
    }

    private fun getRangeOfDates(firstDay: Calendar, lastDay: Calendar): Set<CalendarDay> {
        val dates = mutableSetOf<CalendarDay>()

        do {
            dates.add(firstDay.asCalendarDay())
            firstDay.add(Calendar.DAY_OF_MONTH, 1)
        } while (firstDay.time.before(lastDay.time))

        return dates
    }

}
