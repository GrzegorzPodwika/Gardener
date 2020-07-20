package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.MachinesChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.MachineViewModel
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

//Class No5 - Machines
class MachineFragment : Fragment(), OnDeleteItemListener {
    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val gardenID: Long by lazy {
        MachineViewModel.fromBundle(requireArguments())
    }
    private val viewModelMainMachines: MachineViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private val viewModelReceivedMachines: MachinesChildViewModel by lazy {
        ViewModelProvider(this).get(MachinesChildViewModel::class.java)
    }

    private val receivedMachines: RealmResults<MachineRealm> by lazy {
        viewModelReceivedMachines.getListOfMachinesAsRealmResults()
    }

    private val receivedMachineNames = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tools_in_viewpager,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presetRecyclerView()
        presetAddMachineButton()
        setOnAddMachineButtonListener()
        mapReceivedMachinesIntoNames()
        observeListOfAddedMachines()
    }

    private fun presetRecyclerView() {
        binding.recyclerViewAddedTools.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun presetAddMachineButton() {
        binding.materialButtonAddTools.text = getString(R.string.equipment)
        binding.materialButtonAddTools.icon =
            GardenerApp.res.getDrawable(R.drawable.ic_equipment, null)
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
                receivedMachines[index]?.let {
                    val machineToAdd = ItemRealm(it.machineName, it.numberOfMachines)
                    listOfItemRealm.add(machineToAdd)
                }
            }
        }

        viewModelMainMachines.addListOfPickedMachines(listOfItemRealm)
    }

    private fun mapReceivedMachinesIntoNames() {
        receivedMachines.forEach { receivedMachineNames.add(it.machineName) }
    }

    private fun observeListOfAddedMachines() {
        viewModelMainMachines.listOfMachines
            .observe(viewLifecycleOwner, Observer {
                binding.recyclerViewAddedTools.adapter = AddedItemAdapter(it, this)
            })
    }

    override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
        val maxValue: Int = viewModelReceivedMachines.findMaxValueOf(itemName)

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

    override fun onChangeFlagToOpposite(position: Int) {
        viewModelMainMachines.reverseFlagOnMachine(position)
    }

    override fun onDeleteItemClick(id: Long?) {
        viewModelMainMachines.deleteItemFromList(id)
    }

    companion object {
        fun create(gardenID: Long): MachineFragment {
            val fragment = MachineFragment()
            fragment.arguments = MachineViewModel.toBundle(gardenID)
            return fragment
        }

    }

}