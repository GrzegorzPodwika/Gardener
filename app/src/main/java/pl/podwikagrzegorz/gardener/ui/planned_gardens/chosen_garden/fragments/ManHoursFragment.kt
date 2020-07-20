package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.databinding.ExpandableListsOfManHoursBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.ExpandableListAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetAssignWorkerFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetManHoursFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.ManHoursViewModel
import java.util.*

//Class No8 - Man hours
class ManHoursFragment : Fragment() {

    private lateinit var binding: ExpandableListsOfManHoursBinding
    private val gardenID: Long by lazy {
        ManHoursViewModel.fromBundle(requireArguments())
    }
    private val viewModel: ManHoursViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private lateinit var workersList: RealmResults<WorkerRealm>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.expandable_lists_of_man_hours,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnAddWorkerButtonListener()
        setOnAddManHoursButtonListener()
        observeMapOfWorkedHours()
    }

    private fun setOnAddWorkerButtonListener() {
        workersList = viewModel.getReceivedWorkers()

        binding.materialButtonAddWorkers.setOnClickListener {
            SheetAssignWorkerFragment(
                workersList,
                object :
                    SheetAssignWorkerFragment.OnGetListOfWorkersFullNameListener {
                    override fun onGetListOfWorkersFullName(listOfWorkersFullName: List<String>) {
                        viewModel.addListOfWorkersFullNames(listOfWorkersFullName)
                    }
                }).show(childFragmentManager, null)
        }
    }

    private fun setOnAddManHoursButtonListener() {

        binding.materialButtonAddManHours.setOnClickListener {
            SheetManHoursFragment(
                viewModel.getWorkersFullNames(),
                object :
                    SheetManHoursFragment.OnGetListOfWorkedHoursWithPickedDate {
                    override fun onGetListOfWorkedHoursWithPickedDate(
                        listOfWorkedHours: List<Double>,
                        date: Date
                    ) {
                        viewModel.addListOfWorkedHoursWithPickedDate(listOfWorkedHours, date)
                    }
                }).show(childFragmentManager, null)
        }
    }

    private fun observeMapOfWorkedHours() {
        val expandableList = binding.expandableListView

        viewModel.mapOfWorkedHours.observe(
            viewLifecycleOwner,
            Observer { mapOfWorkedHours ->
                expandableList.setAdapter(
                    ExpandableListAdapter(
                        requireContext(), mapOfWorkedHours
                    )
                )
            })
    }

    companion object {
        fun create(gardenID: Long): ManHoursFragment {
            val fragment = ManHoursFragment()
            fragment.arguments = ManHoursViewModel.toBundle(gardenID)
            return fragment
        }
    }
}