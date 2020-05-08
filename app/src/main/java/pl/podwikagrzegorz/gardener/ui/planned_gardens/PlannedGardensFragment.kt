package pl.podwikagrzegorz.gardener.ui.planned_gardens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.app_bar_main.*
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.BasicGarden
import pl.podwikagrzegorz.gardener.data.pojo.Period
import pl.podwikagrzegorz.gardener.databinding.FragmentPlannedGardensBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add.AddGardenFragment
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
        var isAddedGarden: Boolean = false
        if (args.flowPeriod != null)
            isAddedGarden = true

        if (isAddedGarden) {
            val basicGardenToAdd = BasicGarden()
            basicGardenToAdd.gardenTitle = args.flowGardenTitle
            basicGardenToAdd.phoneNumber = args.flowPhoneNumber
            basicGardenToAdd.period = args.flowPeriod
            basicGardenToAdd.isGarden = args.flowIsGarden
            basicGardenToAdd.snapshotPath = args.flowSnapshotPath
            basicGardenToAdd.latitude = args.flowLatitude.toDouble()
            basicGardenToAdd.longitude = args.flowLongitude.toDouble()

            plannedGardensViewModel.addBasicGarden(basicGardenToAdd)
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

    companion object {
        const val LOG = "LOG"
    }

    override fun onDeleteItemClick(id: Long?){
        val fragmentDialog = DeleteBasicGardenDialog(requireContext(), object : DeleteBasicGardenDialog.NoticeDialogListener{
            override fun onDialogClick(isClickedPositive: Boolean) {
               if (isClickedPositive)
                   plannedGardensViewModel.deleteBasicGarden(id)
            }
        })
        fragmentDialog.show(childFragmentManager, null)
    }

}
