package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.MachinesChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.SheetMachinesFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.MachineViewModel

//Class No5 - Machines
@AndroidEntryPoint
class MachineFragment : Fragment() {

    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val viewModelMainMachines: MachineViewModel by viewModels()
    private lateinit var machineAdapter: AddedItemAdapter

    private val receivedMachinesViewModel: MachinesChildViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentToolsInViewpagerBinding.inflate(inflater, container, false)
        receivedMachinesViewModel.preInitialize()

        connectRecyclerViewWithQuery()
        setUpBinding()
        presetAddMachineButton()
        setOnAddMachineButtonListener()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(viewModelMainMachines.getTakenMachinesQuerySortedByActivity(), Item::class.java)
            .setLifecycleOwner(this)
            .build()

        machineAdapter = AddedItemAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModelMainMachines.deleteItemFromList(childDocumentId = documentId)
            }

            override fun onChangeFlagToOpposite(documentId: String, isActive: Boolean) {
                viewModelMainMachines.reverseFlagOnMachine(childDocumentId = documentId, isActive)
            }

            override fun onChangeNumberOfItems(
                documentId: String,
                currentNumberOfItems: Int,
                maxNumberOfItems: Int
            ) {
                showPickNumberDialog(documentId, currentNumberOfItems, maxNumberOfItems)
            }
        })
    }

    private fun showPickNumberDialog(
        documentId: String,
        currentNumberOfItems: Int,
        maxNumberOfItems: Int
    ) {
        PickNumberDialog(currentNumberOfItems, maxNumberOfItems
        ) { chosenNumber ->
            viewModelMainMachines.updateNumberOfMachines(
                documentId,
                chosenNumber
            )
        }.show(childFragmentManager, null)
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
            ResourcesCompat.getDrawable(GardenerApp.res, R.drawable.ic_equipment, null)
    }

    private fun setOnAddMachineButtonListener() {
        binding.materialButtonAddTools.setOnClickListener {
            SheetMachinesFragment(
                receivedMachinesViewModel.listOfMachines,
                object : SheetMachinesFragment.OnGetListOfPickedItemsListener {
                    override fun onGetListOfPickedItems(listOfPickedItems: List<Item>) {
                        viewModelMainMachines.addListOfPickedMachines(listOfPickedItems)
                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    companion object {
        fun create(gardenTitle: String): MachineFragment {
            val fragment = MachineFragment()
            fragment.arguments = toBundle(gardenTitle)
            return fragment
        }
    }

}




/*
    private fun addListOfPickedMachinesToMainList(listOfPickedItems: List<Boolean>) {
        val listOfItemRealm = mutableListOf<Item>()
        val receivedMachines = receivedMachinesViewModel.listOfMachines


        for (index in listOfPickedItems.indices) {
            if (listOfPickedItems[index]) {
                val machineToAdd = Item(
                    receivedMachines[index].machineName,
                    receivedMachines[index].numberOfMachines
                )
                listOfItemRealm.add(machineToAdd)
            }
        }

        viewModelMainMachines.addListOfPickedMachines(listOfItemRealm)
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
    }*/
