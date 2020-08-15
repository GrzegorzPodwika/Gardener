package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import pl.podwikagrzegorz.gardener.databinding.FragmentDescriptionBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.DescriptionViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory

class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentDescriptionBinding
    private val gardenID: Long by lazy {
        DescriptionViewModel.fromBundle(requireArguments())
    }
    private val viewModel: DescriptionViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private val descriptionAdapter: SingleItemAdapter by lazy {
        SingleItemAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteDescriptionFromDb(id)
            }

            override fun onChangeFlagToOpposite(position: Int) {
                reverseFlagOnDescription(position)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDescriptionBinding.inflate(inflater, container, false)

        setUpViewModelWithBinding()
        observeIsAddedDescription()
        observeListOfDescriptions()

        return binding.root
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            descriptionViewModel = viewModel
            recyclerViewDescriptionList.adapter = descriptionAdapter
            userDescription = ""
        }
    }

    private fun observeIsAddedDescription() {
        viewModel.eventOnAddedDescription.observe(viewLifecycleOwner, Observer { isAdded ->
            if (isAdded) {
                cleanUp()
            }
        })
    }

    private fun observeListOfDescriptions() {
        viewModel.listOfDescriptions
            .observe(viewLifecycleOwner, Observer { listOfDescriptions ->
                descriptionAdapter.submitList(listOfDescriptions)
            })
    }

    private fun cleanUp() {
        clearView()
        setFocusToEditTextView()
        viewModel.onAddDescriptionComplete()
    }

    private fun clearView() {
        binding.editTextDescriptionName.text.clear()
    }

    private fun setFocusToEditTextView() {
        binding.editTextDescriptionName.requestFocus()
    }

    private fun deleteDescriptionFromDb(id: Long) {
        viewModel.deleteDescriptionFromList(id)
    }

    private fun reverseFlagOnDescription(position: Int) {
        viewModel.reverseFlagOnDescription(position)
    }

    companion object {
        fun create(gardenID: Long): DescriptionFragment {
            val fragment = DescriptionFragment()
            fragment.arguments = DescriptionViewModel.toBundle(gardenID)
            return fragment
        }

    }
}