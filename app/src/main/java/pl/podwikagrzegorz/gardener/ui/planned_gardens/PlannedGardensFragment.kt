package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden
import pl.podwikagrzegorz.gardener.data.domain.Period
import pl.podwikagrzegorz.gardener.databinding.FragmentPlannedGardensBinding
import pl.podwikagrzegorz.gardener.extensions.getAbsoluteFilePath
import pl.podwikagrzegorz.gardener.extensions.snackbar
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.BasicGardenAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.DeleteBasicGardenDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add.AddGardenFragment

@AndroidEntryPoint
class PlannedGardensFragment : Fragment() {

    private lateinit var binding: FragmentPlannedGardensBinding
    private val viewModel: PlannedGardensViewModel by viewModels()
    private lateinit var basicGardenAdapter: BasicGardenAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlannedGardensBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpBindingWithViewModel()
        presetRecyclerView()
        setOnFragmentResultListener()
        observeData()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<BasicGarden>()
            .setQuery(viewModel.getQuerySortedByTimestamp(), BasicGarden::class.java)
            .setLifecycleOwner(this)
            .build()

        basicGardenAdapter = BasicGardenAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                navigateToGardenViewPagerFragment(documentId)
            }

            override fun onLongClick(documentId: String) {
                showDeleteGardenDialog(documentId)
            }
        })
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
        val snapshotName = bundle.getString(AddGardenFragment.REQUEST_UNIQUE_SNAPSHOT_NAME, "")
        val absolutePath = requireContext().getAbsoluteFilePath(snapshotName)

        basicGarden.apply {
            gardenTitle = bundle.getString(AddGardenFragment.REQUEST_GARDEN_TITLE, "")
            phoneNumber = bundle.getInt(AddGardenFragment.REQUEST_PHONE_NUMBER)
            period = bundle.getSerializable(AddGardenFragment.REQUEST_PERIOD) as Period
            isGarden = bundle.getBoolean(AddGardenFragment.REQUEST_IS_GARDEN)
            uniqueSnapshotName = absolutePath
            latitude = bundle.getDouble(AddGardenFragment.REQUEST_LATITUDE)
            longitude = bundle.getDouble(AddGardenFragment.REQUEST_LONGITUDE)
        }

        viewModel.addBasicGarden(basicGarden)
    }

    private fun observeData() {
        observeAddGardenFAB()
        observeInsertData()
    }

    private fun observeAddGardenFAB() {
        viewModel.navigateToAddGarden.observe(viewLifecycleOwner, Observer { hasClicked ->
            if (hasClicked) {
                navigateToAddGardenFragment()
            }
        })
    }

    private fun observeInsertData() {
        viewModel.eventGardenInserted.observe(viewLifecycleOwner, Observer { isSuccess ->
            if (isSuccess) {
                binding.root.snackbar("Insert success")
                viewModel.onShowSuccessSnackbarComplete()
            }
        })
    }

    private fun navigateToAddGardenFragment() {
        val navController = findNavController()
        val action = PlannedGardensFragmentDirections.actionPlannedGardensToAddGarden()
        navController.navigate(action)

        viewModel.onNavigateComplete()
    }


    private fun navigateToGardenViewPagerFragment(documentId: String) {
        val navController = findNavController()
        val action = PlannedGardensFragmentDirections
            .actionNavPlannedGardensToNavGardenViewPagerFragment(documentId)
        navController.navigate(action)
    }

    fun showDeleteGardenDialog(documentId: String) {
        val fragmentDialog = DeleteBasicGardenDialog(requireContext(),
            object :
                DeleteBasicGardenDialog.NoticeDialogListener {
                override fun onDialogClick(isClickedPositive: Boolean) {
                    if (isClickedPositive)
                        viewModel.deleteGarden(documentId)
                }
            })
        fragmentDialog.show(childFragmentManager, null)
    }

}
