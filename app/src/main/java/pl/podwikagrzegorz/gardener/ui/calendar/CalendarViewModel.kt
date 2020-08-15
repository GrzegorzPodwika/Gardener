package pl.podwikagrzegorz.gardener.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.*
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.daos.GardenDAO
import pl.podwikagrzegorz.gardener.data.domain.Period
import pl.podwikagrzegorz.gardener.extensions.asCalendarDay
import java.util.*

class CalendarViewModel : ViewModel() {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val gardenDAO = GardenDAO()

    private val listOfPeriods : List<Period>
    private val _listOfRangeDecorators = MutableLiveData<List<RangeDecorator>>()
    val listOfRangeDecorators : LiveData<List<RangeDecorator>>
        get() = _listOfRangeDecorators

    init {
        listOfPeriods = gardenDAO.getDomainPeriodData()
        initializeListOfRangeDecorators()
    }

    private fun initializeListOfRangeDecorators() {
        uiScope.launch {
            _listOfRangeDecorators.value = calculateRangeDecorators()
        }
    }

    private suspend fun calculateRangeDecorators(): List<RangeDecorator> {
        return withContext(Dispatchers.Default) {
            val listOfDecorators = mutableListOf<RangeDecorator>()

            val firstDay = Calendar.getInstance()
            val lastDay = Calendar.getInstance()

            for (period in listOfPeriods) {
                firstDay.set(period.startYear, period.startMonth - 1, period.startDay)
                lastDay.set(period.endYear, period.endMonth - 1, period.endDay)

                val datesSet = mutableSetOf<CalendarDay>()
                do {
                    datesSet.add(firstDay.asCalendarDay())
                    firstDay.add(Calendar.DAY_OF_MONTH, 1)
                } while (firstDay.time.before(lastDay.time))

                datesSet.add(firstDay.asCalendarDay())

                listOfDecorators.add(RangeDecorator(coloredCircle, datesSet))

            }
            listOfDecorators
        }
    }

    override fun onCleared() {
        gardenDAO.closeRealm()
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {
        private val coloredCircle = GardenerApp.res.getDrawable(R.drawable.calendar_circle, null)
    }
}

/*    private fun getRangeOfDates(firstDay: Calendar, lastDay: Calendar): Set<CalendarDay> {
        val dates = mutableSetOf<CalendarDay>()

        do {
            dates.add(firstDay.asCalendarDay())
            firstDay.add(Calendar.DAY_OF_MONTH, 1)
        } while (firstDay.time.before(lastDay.time))

        dates.add(firstDay.asCalendarDay())

        return dates
    }*/
