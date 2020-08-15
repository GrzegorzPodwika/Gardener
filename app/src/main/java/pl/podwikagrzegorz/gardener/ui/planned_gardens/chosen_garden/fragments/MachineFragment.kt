package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.daos.MachineDAO
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.MachinesChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.MachineViewModel

// TODO ogarnac MAchineFragment, ManHours..ToolFragment @see - ToolFramgnet9
//Class No5 - Machines
class MachineFragment : Fragment() {

    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val gardenID: Long by lazy {
        MachineViewModel.fromBundle(requireArguments())
    }
    private val viewModelMainMachines: MachineViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }

    private lateinit var receivedMachines: List<Machine>
    private val receivedMachineNames = mutableListOf<String>()

    private val machineAdapter: AddedItemAdapter by lazy {
        AddedItemAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteMachineFromDb(id)
            }

            override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
                changeNumberOfItems(currentValue, position, itemName)
            }

            override fun onChangeFlagToOpposite(position: Int) {
                changeFlagToOpposite(position)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentToolsInViewpagerBinding.inflate(inflater, container, false)

        setUpBinding()
        presetAddMachineButton()
        fetchReceivedMachines()
        mapReceivedMachinesIntoNames()
        setOnAddMachineButtonListener()
        observeListOfAddedMachines()

        return binding.root
    }

    private fun setUpBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerViewAddedTools.adapter = machineAdapter
        }
    }

    private fun presetAddMachineButton() {
        binding.materialButtonAddTools.text = getString(R.string.equipment)
        binding.materialButtonAddTools.icon =
            GardenerApp.res.getDrawable(R.drawable.ic_equipment, null)
    }


    private fun fetchReceivedMachines() {
        val machineDAO = MachineDAO()
        receivedMachines = machineDAO.getDomainData()
        machineDAO.closeRealm()
    }

    private fun mapReceivedMachinesIntoNames() {
        receivedMachines.forEach { receivedMachineNames.add(it.machineName) }
    }

    private fun setOnAddMachineButtonListener() {
        binding.materialButtonAddTools.setOnClickListener {
            SheetToolsFragment(
                receivedMachineNames,
                object : SheetToolsFragment.OnGetListOfPickedItemsListener {
                    override fun onGetListOfPickedItems(listOfPickedItems: List<Boolean>) {
                        addListOfPickedMachinesToMainList(listOfPickedItems)
                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    private fun addListOfPickedMachinesToMainList(listOfPickedItems: List<Boolean>) {
        val listOfItemRealm = mutableListOf<ItemRealm>()

        for (index in listOfPickedItems.indices) {
            if (listOfPickedItems[index]) {
                val machineToAdd = ItemRealm(receivedMachines[index].machineName, receivedMachines[index].numberOfMachines)
                listOfItemRealm.add(machineToAdd)
            }
        }

        viewModelMainMachines.addListOfPickedMachines(listOfItemRealm)
    }

    private fun observeListOfAddedMachines() {
        viewModelMainMachines.listOfMachines.observe(viewLifecycleOwner, Observer {listOfAddedMachines ->
                machineAdapter.submitList(listOfAddedMachines)
            })
    }

    private fun deleteMachineFromDb(id: Long) {
        viewModelMainMachines.deleteItemFromList(id)
    }

    private fun changeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
        val seekingMachine = receivedMachines.find { it.machineName == itemName }
        val maxValue: Int = seekingMachine?.numberOfMachines ?: GardenerApp.MAX_NUMBER_OF_MACHINES

        PickNumberDialog(
            currentValue,
            maxValue,
            object :
                PickNumberDialog.OnChosenNumberListener {
                override fun onChosenNumber(chosenNumber: Int) {
                    viewModelMainMachines.updateNumberOfMachines(chosenNumber, position)
                }
            }).show(childFragmentManager, null)
    }

    private fun changeFlagToOpposite(position: Int) {
        viewModelMainMachines.reverseFlagOnMachine(position)
    }

    companion object {
        fun create(gardenID: Long): MachineFragment {
            val fragment = MachineFragment()
            fragment.arguments = MachineViewModel.toBundle(gardenID)
            return fragment
        }

    }

}