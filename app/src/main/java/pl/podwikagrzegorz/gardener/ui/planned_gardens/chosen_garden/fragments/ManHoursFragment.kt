package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import pl.podwikagrzegorz.gardener.data.daos.WorkerDAO
import pl.podwikagrzegorz.gardener.data.domain.Worker
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
    private val manHoursViewModel: ManHoursViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private lateinit var listOfWorkers: List<Worker>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpandableListsOfManHoursBinding.inflate(inflater, container, false)

        setUpViewModelWithBinding()
        fetchListOfWorkers()
        setOnAddWorkerButtonListener()
        setOnAddManHoursButtonListener()
        observeMapOfWorkedHours()

        return binding.root
    }

    private fun fetchListOfWorkers() {
        val workerDAO = WorkerDAO()
        listOfWorkers = workerDAO.getDomainData()
        workerDAO.closeRealm()
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            manHoursViewModel = manHoursViewModel
        }
    }

    private fun setOnAddWorkerButtonListener() {

        binding.materialButtonAddWorkers.setOnClickListener {
            SheetAssignWorkerFragment(
                listOfWorkers,
                object : SheetAssignWorkerFragment.OnGetListOfWorkersFullNameListener {
                    override fun onGetListOfWorkersFullName(listOfWorkersFullName: List<String>) {
                        manHoursViewModel.addListOfWorkersFullNames(listOfWorkersFullName)
                    }
                }).show(childFragmentManager, null)
        }
    }

    private fun setOnAddManHoursButtonListener() {

        binding.materialButtonAddManHours.setOnClickListener {
            SheetManHoursFragment(
                manHoursViewModel.workersFullNames,
                object :
                    SheetManHoursFragment.OnGetListOfWorkedHoursWithPickedDate {
                    override fun onGetListOfWorkedHoursWithPickedDate(
                        listOfWorkedHours: List<Double>,
                        date: Date
                    ) {
                        manHoursViewModel.addListOfWorkedHoursWithPickedDate(
                            listOfWorkedHours,
                            date
                        )
                    }
                }).show(childFragmentManager, null)
        }
    }

    private fun observeMapOfWorkedHours() {
        manHoursViewModel.mapOfWorkedHours.observe(viewLifecycleOwner,
            Observer { mapOfWorkedHours ->
                binding.expandableListView.setAdapter(ExpandableListAdapter(requireContext(), mapOfWorkedHours))
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