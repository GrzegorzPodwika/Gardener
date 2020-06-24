package pl.podwikagrzegorz.gardener.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentCalendarBinding
import pl.podwikagrzegorz.gardener.extensions.asCalendarDay
import pl.podwikagrzegorz.gardener.extensions.convertMonthToName
import java.time.ZoneId
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

        //preInitialiseCalendar()
        setCalendarListener()
        colorPeriodsOfWork()
    }

    private fun preInitialiseCalendar() {
        val yearBefore = Calendar.getInstance()
        val yearNext = Calendar.getInstance()

        yearBefore.set(Calendar.YEAR, -1)
        yearNext.set(Calendar.YEAR, 1)


        calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(yearBefore.get(Calendar.YEAR), yearBefore.get(Calendar.MONTH), yearBefore.get(Calendar.DAY_OF_MONTH)))
            .setMaximumDate(CalendarDay.from(yearNext.get(Calendar.YEAR), yearNext.get(Calendar.MONTH), yearNext.get(Calendar.DAY_OF_MONTH)))
            .commit()
    }

    private fun setCalendarListener() {

        calendarView.setOnDateLongClickListener { widget, date ->

        }

    }

    private fun colorPeriodsOfWork() {
        val periodsList: List<PeriodRealm?> = calendarViewModel.getPeriodsList()
        val firstDay = Calendar.getInstance()
        val lastDay = Calendar.getInstance()

        for (index in periodsList.indices) {
            periodsList[index]?.let {
                firstDay.set(it.startYear, it.startMonth - 1, it.startDay)
                lastDay.set(it.endYear, it.endMonth - 1, it.endDay)
                val datesSet = getRangesDate(firstDay, lastDay)

                calendarView.addDecorator(
                    RangeDecorator(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.calendar_circle
                        ),
                        datesSet
                    )
                )
            }
        }
    }

    private fun getRangesDate(firstDay: Calendar, lastDay: Calendar): Set<CalendarDay> {
        val dates = mutableSetOf<CalendarDay>()

        while (firstDay.time.before(lastDay.time)) {
            dates.add(firstDay.asCalendarDay())
            firstDay.add(Calendar.DAY_OF_MONTH, 1)
        }
        dates.add(firstDay.asCalendarDay())

        return dates
    }

}
