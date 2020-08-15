package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import pl.podwikagrzegorz.gardener.databinding.FragmentAddedGardenBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.BasicGardenViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory

class BasicGardenFragment : Fragment() {

    private lateinit var binding: FragmentAddedGardenBinding
    private val gardenID: Long by lazy {
        BasicGardenViewModel.fromBundle(requireArguments())
    }
    private val viewModel: BasicGardenViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddedGardenBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        observeEventsFromViewModel()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            basicGardenViewModel = viewModel
        }
    }

    private fun observeEventsFromViewModel() {
        viewModel.eventCallToClient.observe(
            viewLifecycleOwner,
            Observer { clickedCallButton ->
                if (clickedCallButton) {
                    callToClient()
                }
            })

        viewModel.eventNavigateToWorkPlace.observe(
            viewLifecycleOwner,
            Observer { clickedNavigateButton ->
                if (clickedNavigateButton) {
                    navigateToWorkPlace()
                }
            })
    }

    private fun callToClient() {
        val phoneNumber = viewModel.getPhoneNumber()
        val implicitIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${phoneNumber}"))
        startActivity(implicitIntent)

        viewModel.onCallComplete()
    }

    private fun navigateToWorkPlace() {
        val navigationIntentUri = viewModel.getNavigationIntentUri()
        val packageManager = requireContext().packageManager

        val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }

        viewModel.onNavigateComplete()
    }

    companion object {
        fun create(gardenID: Long): BasicGardenFragment {
            val fragment = BasicGardenFragment()
            fragment.arguments = BasicGardenViewModel.toBundle(gardenID)
            return fragment
        }
    }
}