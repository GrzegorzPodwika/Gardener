package pl.podwikagrzegorz.gardener.ui.calendar

import androidx.core.content.res.ResourcesCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.*
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Period
import pl.podwikagrzegorz.gardener.data.repo.CalendarRepository
import pl.podwikagrzegorz.gardener.extensions.asCalendarDay
import java.util.*

class CalendarViewModel @ViewModelInject constructor(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    private var listOfPeriods : List<Period> = emptyList()
    private val _listOfRangeDecorators = MutableLiveData<List<RangeDecorator>>()
    val listOfRangeDecorators : LiveData<List<RangeDecorator>>
        get() = _listOfRangeDecorators

    init {
        initializeViewModel()
    }

    private fun initializeViewModel() {
        viewModelScope.launch {
            fetchListOfPeriods()
            transformPeriodsIntoRangeDecorators()
        }
    }

    private suspend fun fetchListOfPeriods() {
        listOfPeriods = calendarRepository.getAllPeriods()
    }

    private suspend fun transformPeriodsIntoRangeDecorators() {
        _listOfRangeDecorators.postValue(calculateRangeDecorators())
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


    companion object {
        private val coloredCircle = ResourcesCompat.getDrawable(GardenerApp.res, R.drawable.calendar_circle, null)
    }
}

