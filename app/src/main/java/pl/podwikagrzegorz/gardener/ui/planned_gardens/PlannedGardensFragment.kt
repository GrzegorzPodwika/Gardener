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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.data.realm.PeriodRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentPlannedGardensBinding
import pl.podwikagrzegorz.gardener.extensions.mapIntoPeriodRealm
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.BasicGardenAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.DeleteBasicGardenDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add.AddGardenFragment


//TODO 1. clean codu w tym klasach co przerabialem, moze jkaies poprawki?
// 2.
// 3. w photofragment dodac zeby wyswietlalo sie w dialogu cale zdjecie
// 4. itd do porzygu
class PlannedGardensFragment : Fragment(),
    OnClickItemListener {

    private val plannedGardensViewModel: PlannedGardensViewModel by lazy {
        ViewModelProvider(this).get(PlannedGardensViewModel::class.java)
    }
    private lateinit var binding: FragmentPlannedGardensBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_planned_gardens, container, false)

        setOnAddGardenButtonListener()
        setOnFragmentResultListener()
        presetRecyclerView()
        observeListOfBasicGardens()

        return binding.root
    }

    private fun setOnAddGardenButtonListener() {
        binding.fabAddGarden.setOnClickListener {
            val navController = findNavController()
            val action = PlannedGardensFragmentDirections.actionPlannedGardensToAddGarden()
            navController.navigate(action)
        }
    }

    private fun setOnFragmentResultListener() {
        setFragmentResultListener(AddGardenFragment.KEY_DATA_FROM_ADD_GARDEN_FRAGMENT) { resultKey: String, bundle: Bundle ->
            getArgumentsFromAddedFragment(bundle)
        }
    }

    private fun getArgumentsFromAddedFragment(bundle: Bundle) {
        val basicGardenRealm = BasicGardenRealm()
        val period : Period = bundle.getSerializable(AddGardenFragment.REQUEST_PERIOD) as Period

        basicGardenRealm.gardenTitle = bundle.getString(AddGardenFragment.REQUEST_GARDEN_TITLE, "")
        basicGardenRealm.phoneNumber = bundle.getInt(AddGardenFragment.REQUEST_PHONE_NUMBER)
        basicGardenRealm.period = period.mapIntoPeriodRealm()
        basicGardenRealm.isGarden = bundle.getBoolean(AddGardenFragment.REQUEST_IS_GARDEN)
        basicGardenRealm.uniqueSnapshotName = bundle.getString(AddGardenFragment.REQUEST_UNIQUE_SNAPSHOT_NAME, "")
        basicGardenRealm.latitude = bundle.getDouble(AddGardenFragment.REQUEST_LATITUDE)
        basicGardenRealm.longitude = bundle.getDouble(AddGardenFragment.REQUEST_LONGITUDE)

        plannedGardensViewModel.addBasicGarden(basicGardenRealm)
    }

    private fun presetRecyclerView() {
        binding.recyclerViewPlannedGardens.layoutManager = LinearLayoutManager(requireContext())
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

    private fun observeListOfBasicGardens() {
        plannedGardensViewModel.listOfBasicGardens.observe(viewLifecycleOwner,
            Observer { listOfBasicGardens ->
                binding.recyclerViewPlannedGardens.adapter =
                    BasicGardenAdapter(
                        listOfBasicGardens,
                        this
                    )
            }
        )
    }

    override fun onClickListener(id: Long) {
        navigateToGardenViewPagerFragment(id)
    }

    private fun navigateToGardenViewPagerFragment(gardenId: Long) {
        val navController = findNavController()
        val action = prepareArgsForGardenViewPagerFragment(gardenId)
        navController.navigate(action)
    }

    private fun prepareArgsForGardenViewPagerFragment(gardenId: Long): NavDirections =
        PlannedGardensFragmentDirections.actionNavPlannedGardensToNavGardenViewPagerFragment(
            gardenId
        )

    override fun onLongClickListener(id: Long) {
        val fragmentDialog =
            DeleteBasicGardenDialog(
                requireContext(),
                object :
                    DeleteBasicGardenDialog.NoticeDialogListener {
                    override fun onDialogClick(isClickedPositive: Boolean) {
                        if (isClickedPositive)
                            plannedGardensViewModel.deleteGarden(id)
                    }
                })
        fragmentDialog.show(childFragmentManager, null)
    }

    companion object {
        const val LOG = "LOG"
        const val GARDEN_ID = "GARDEN_ID"
    }
}
