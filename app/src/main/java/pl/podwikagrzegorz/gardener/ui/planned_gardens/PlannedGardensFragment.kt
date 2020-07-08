package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentPlannedGardensBinding
import pl.podwikagrzegorz.gardener.extensions.mapIntoPeriodRealm
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.BasicGardenAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden.DeleteBasicGardenDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.GardenFragmentActivity

class PlannedGardensFragment : Fragment(),
    OnClickItemListener {

    private lateinit var plannedGardensViewModel: PlannedGardensViewModel
    private lateinit var binding: FragmentPlannedGardensBinding
    private val args: PlannedGardensFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        plannedGardensViewModel = ViewModelProvider(this).get(PlannedGardensViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_planned_gardens, container, false)
        binding.fabAddGarden.setOnClickListener {
            val navController = findNavController()
            val action = PlannedGardensFragmentDirections.actionPlannedGardensToAddGarden()
            navController.navigate(action)
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgumentsFromAddedFragment()
    }

    private fun getArgumentsFromAddedFragment() {
        // if args.flowPeriod == null means that we don't get any arguments
        var isAddedGarden = false
        if (args.flowPeriod != null)
            isAddedGarden = true

        if (isAddedGarden) {
            val basicGardenRealm = BasicGardenRealm()
            basicGardenRealm.gardenTitle = args.flowGardenTitle
            basicGardenRealm.phoneNumber = args.flowPhoneNumber
            basicGardenRealm.period = args.flowPeriod?.mapIntoPeriodRealm()
            basicGardenRealm.isGarden = args.flowIsGarden
            basicGardenRealm.snapshotPath = args.flowSnapshotPath
            basicGardenRealm.latitude = args.flowLatitude.toDouble()
            basicGardenRealm.longitude = args.flowLongitude.toDouble()

            plannedGardensViewModel.addBasicGarden(basicGardenRealm)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presetRecyclerView()
        observeListOfBasicGardens()
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
        startGardenFragmentActivity(id)
    }

    private fun startGardenFragmentActivity(id: Long) {
        val intent = Intent(requireContext(), GardenFragmentActivity::class.java)
        intent.putExtra(GARDEN_ID, id)
        startActivity(intent)
    }

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
