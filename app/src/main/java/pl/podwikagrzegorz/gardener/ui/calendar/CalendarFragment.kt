package pl.podwikagrzegorz.gardener.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentCalendarBinding

@AndroidEntryPoint
class CalendarFragment : Fragment() {

    private val calendarViewModel: CalendarViewModel by viewModels()
    private lateinit var binding: FragmentCalendarBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        setUpBinding()
        observeListOfDecorators()

        return binding.root
    }

    private fun setUpBinding() {
        binding.lifecycleOwner = viewLifecycleOwner

    }

    private fun observeListOfDecorators() {
        calendarViewModel.listOfRangeDecorators.observe(viewLifecycleOwner, Observer { listOfRangeDecorators ->
            binding.calendar.addDecorators(listOfRangeDecorators)
        })
    }

}
