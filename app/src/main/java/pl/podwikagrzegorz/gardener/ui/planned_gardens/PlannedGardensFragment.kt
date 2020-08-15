package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.data.domain.Period
import pl.podwikagrzegorz.gardener.databinding.FragmentPlannedGardensBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.BasicGardenAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.DeleteBasicGardenDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add.AddGardenFragment

class PlannedGardensFragment : Fragment() {

    private lateinit var binding: FragmentPlannedGardensBinding
    private val viewModel: PlannedGardensViewModel by lazy {
        ViewModelProvider(this).get(PlannedGardensViewModel::class.java)
    }
    private val basicGardenAdapter: BasicGardenAdapter by lazy {
        BasicGardenAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                navigateToGardenViewPagerFragment(id)
            }

            override fun onLongClick(id: Long) {
                showDeleteGardenDialog(id)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_planned_gardens, container, false)

        setUpBindingWithViewModel()
        presetRecyclerView()
        setOnFragmentResultListener()
        observeData()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            plannedGardensViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerViewPlannedGardens.adapter = basicGardenAdapter
        }
    }

    private fun presetRecyclerView() {
        binding.recyclerViewPlannedGardens.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fabAddGarden.visibility == View.VISIBLE)
                    binding.fabAddGarden.hide()
                else if (dy < 0 && binding.fabAddGarden.visibility != View.VISIBLE)
                    binding.fabAddGarden.show()
            }
        })
    }

    private fun setOnFragmentResultListener() {
        setFragmentResultListener(AddGardenFragment.KEY_DATA_FROM_ADD_GARDEN_FRAGMENT) { requestKey, bundle: Bundle ->
            if (requestKey == AddGardenFragment.KEY_DATA_FROM_ADD_GARDEN_FRAGMENT) {
                getArgumentsFromAddedFragment(bundle)
            }
        }
    }

    private fun getArgumentsFromAddedFragment(bundle: Bundle) {
        val basicGarden = BasicGarden()

        basicGarden.apply {
            gardenTitle = bundle.getString(AddGardenFragment.REQUEST_GARDEN_TITLE, "")
            phoneNumber = bundle.getInt(AddGardenFragment.REQUEST_PHONE_NUMBER)
            period = bundle.getSerializable(AddGardenFragment.REQUEST_PERIOD) as Period
            isGarden = bundle.getBoolean(AddGardenFragment.REQUEST_IS_GARDEN)
            uniqueSnapshotName = bundle.getString(AddGardenFragment.REQUEST_UNIQUE_SNAPSHOT_NAME, "")
            latitude = bundle.getDouble(AddGardenFragment.REQUEST_LATITUDE)
            longitude = bundle.getDouble(AddGardenFragment.REQUEST_LONGITUDE)
        }

        viewModel.addBasicGarden(basicGarden)
    }

    private fun observeData() {
        observeAddGardenFAB()
        observeListOfBasicGardens()
    }

    private fun observeAddGardenFAB() {
        viewModel.navigateToAddGarden.observe(viewLifecycleOwner, Observer { hasClicked ->
            if (hasClicked) {
                navigateToAddGardenFragment()
            }
        })
    }

    private fun navigateToAddGardenFragment() {
        val navController = findNavController()
        val action = PlannedGardensFragmentDirections.actionPlannedGardensToAddGarden()
        navController.navigate(action)
        viewModel.onNavigateComplete()
    }


    private fun observeListOfBasicGardens() {
        viewModel.listOfBasicGardens.observe(
            viewLifecycleOwner,
            Observer { listOfBasicGardens ->
                basicGardenAdapter.submitList(listOfBasicGardens)
            }
        )
    }

    private fun navigateToGardenViewPagerFragment(gardenId: Long) {
        val navController = findNavController()
        val action = PlannedGardensFragmentDirections.actionNavPlannedGardensToNavGardenViewPagerFragment(gardenId)
        navController.navigate(action)
    }

    fun showDeleteGardenDialog(id: Long) {
        val fragmentDialog =
            DeleteBasicGardenDialog(
                requireContext(),
                object :
                    DeleteBasicGardenDialog.NoticeDialogListener {
                    override fun onDialogClick(isClickedPositive: Boolean) {
                        if (isClickedPositive)
                            viewModel.deleteGarden(id)
                    }
                })
        fragmentDialog.show(childFragmentManager, null)
    }

    companion object {
        const val GARDEN_ID = "GARDEN_ID"
    }
}
