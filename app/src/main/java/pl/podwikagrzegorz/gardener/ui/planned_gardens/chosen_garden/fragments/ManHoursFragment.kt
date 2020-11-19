package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.data.domain.ManHours
import pl.podwikagrzegorz.gardener.databinding.ExpandableListsOfManHoursBinding
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.EditDeleteWorkedHoursListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.ExpandableListAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.EditManHoursDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.SheetAssignWorkerFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.SheetManHoursFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.ManHoursViewModel
import pl.podwikagrzegorz.gardener.ui.workers.WorkersViewModel

//Class No8 - Man hours
@AndroidEntryPoint
class ManHoursFragment : Fragment() {

    private lateinit var binding: ExpandableListsOfManHoursBinding
    private val manHoursViewModel: ManHoursViewModel by viewModels()
    private val workerViewModel: WorkersViewModel by viewModels()
    private val editDeleteWorkedHoursListener = object: EditDeleteWorkedHoursListener {
        override fun onEditClick(
            manHours: ManHours,
            workerDocumentId: String,
            manHoursDocumentId: String
        ) {
            showEditManHoursDialog(manHours, workerDocumentId, manHoursDocumentId)
        }

        override fun onDeleteClick(
            workerDocumentId: String,
            manHoursDocumentId: String
        ) {
            manHoursViewModel.deleteManHours(workerDocumentId, manHoursDocumentId)
        }

    }

    private fun showEditManHoursDialog(
        manHours: ManHours,
        workerDocumentId: String,
        manHoursDocumentId: String
    ) {
        EditManHoursDialog(manHours) {updatedManHours ->
            manHoursViewModel.updateManHours(updatedManHours, workerDocumentId, manHoursDocumentId)
        }.show(childFragmentManager, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ExpandableListsOfManHoursBinding.inflate(inflater, container, false)
        workerViewModel.preInitialize()

        setUpViewModelWithBinding()
        setOnAddWorkerButtonListener()
        setOnAddManHoursButtonListener()
        observeMapOfWorkedHours()

        return binding.root
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            manHoursViewModel = manHoursViewModel
        }
    }

    private fun setOnAddWorkerButtonListener() {

        binding.materialButtonAddWorkers.setOnClickListener {
            SheetAssignWorkerFragment(workerViewModel.listOfWorkers) { listOfPickedWorkers ->
                manHoursViewModel.addListOfPickedWorkers(listOfPickedWorkers)
            }.show(childFragmentManager, null)
        }
    }

    private fun setOnAddManHoursButtonListener() {
        binding.materialButtonAddManHours.setOnClickListener {
            SheetManHoursFragment(manHoursViewModel.workersFullNames) { listOfWorkedHours, date ->
                manHoursViewModel.addListOfWorkedHoursWithPickedDate(
                    listOfWorkedHours,
                    date
                )
            }.show(childFragmentManager, null)
        }
    }


    private fun observeMapOfWorkedHours() {
        manHoursViewModel.mapOfWorkedHours.observe(viewLifecycleOwner,
            Observer { mapOfWorkedHours ->
                binding.expandableListView.setAdapter(
                    ExpandableListAdapter(
                        requireContext(),
                        mapOfWorkedHours,
                        editDeleteWorkedHoursListener
                    ) { parentWorkerDocumentId ->
                        manHoursViewModel.deleteParentWorker(parentWorkerDocumentId)
                    }
                )
            })
    }

    companion object {
        fun create(gardenTitle: String): ManHoursFragment {
            val fragment = ManHoursFragment()
            fragment.arguments = toBundle(gardenTitle)
            return fragment
        }
    }
}