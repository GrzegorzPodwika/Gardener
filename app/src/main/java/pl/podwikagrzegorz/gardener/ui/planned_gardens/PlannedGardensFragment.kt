package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.BasicGardenRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentPlannedGardensBinding
import pl.podwikagrzegorz.gardener.extensions.mapIntoPeriodRealm
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.GardenFragmentActivity
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class PlannedGardensFragment : Fragment(), OnDeleteItemListener {

    private lateinit var plannedGardensViewModel: PlannedGardensViewModel
    private lateinit var binding: FragmentPlannedGardensBinding
    private val args: PlannedGardensFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(LOG, "onCreateView")

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.i(LOG, "onActivityCreated")

        getArgumentsFromAddedFragment()

        plannedGardensViewModel.getBasicGardenData().observe(viewLifecycleOwner,
            Observer { gardens ->
                binding.recyclerViewPlannedGardens.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = BasicGardenAdapter(gardens, this)
                }
            }
        )
    }

    private fun getArgumentsFromAddedFragment() {
        // if args.flowPeriod == null means that we don't get any arguments
        var isAddedGarden = false
        if (args.flowPeriod != null)
            isAddedGarden = true

        //TODO("Dodawac now GardenRealm a nie BasicGardenRealm")

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

    override fun onStart() {
        super.onStart()
        Log.i(LOG, "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.i(LOG, "onStop\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.i(LOG, "onDestroyView\n")
    }

    override fun onDeleteItemClick(id: Long?) {
        startGardenFragmentActivity(id)
    }

    private fun startGardenFragmentActivity(id: Long?) {
        val intent = Intent(requireContext(), GardenFragmentActivity::class.java)
        intent.putExtra(GARDEN_ID, id ?: 0)
        startActivity(intent)
    }

    override fun onDeleteItemLongClick(id: Long?) {
        val fragmentDialog = DeleteBasicGardenDialog(requireContext(), object : DeleteBasicGardenDialog.NoticeDialogListener{
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
