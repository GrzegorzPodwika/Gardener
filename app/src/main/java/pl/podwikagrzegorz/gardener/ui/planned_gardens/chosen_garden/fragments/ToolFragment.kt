package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.data.daos.ToolDAO
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.ToolViewModel

//Class No4 - Tools
class ToolFragment : Fragment() {
    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val gardenID: Long by lazy {
        ToolViewModel.fromBundle(requireArguments())
    }
    private val viewModelMainTools: ToolViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }

    private lateinit var receivedTools: List<Tool>
    private val receivedToolNames = mutableListOf<String>()

    private val toolAdapter: AddedItemAdapter by lazy {
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
        fetchReceivedTools()
        mapReceivedToolsIntoNames()
        setOnAddToolsButtonListener()
        observeListOfAddedTools()

        return binding.root
    }

    private fun setUpBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerViewAddedTools.adapter = toolAdapter
        }
    }

    private fun fetchReceivedTools() {
        val toolDAO = ToolDAO()
        receivedTools = toolDAO.getDomainData()
        toolDAO.closeRealm()
    }

    private fun mapReceivedToolsIntoNames() {
        receivedTools.forEach { receivedToolNames.add(it.toolName) }
    }

    private fun setOnAddToolsButtonListener() {
        binding.materialButtonAddTools.setOnClickListener {
            SheetToolsFragment(
                receivedToolNames,
                object : SheetToolsFragment.OnGetListOfPickedItemsListener {
                    override fun onGetListOfPickedItems(listOfPickedItems: List<Boolean>) {
                        addListOfPickedToolsToMainList(listOfPickedItems)
                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    private fun addListOfPickedToolsToMainList(listOfPickedItems: List<Boolean>) {
        val listOfItemRealm = mutableListOf<ItemRealm>()

        for (index in listOfPickedItems.indices) {
            if (listOfPickedItems[index]) {
                val toolToAdd =
                    ItemRealm(receivedTools[index].toolName, receivedTools[index].numberOfTools)
                listOfItemRealm.add(toolToAdd)
            }
        }

        viewModelMainTools.addListOfPickedTools(listOfItemRealm)
    }

    private fun observeListOfAddedTools() {
        viewModelMainTools.listOfTools.observe(viewLifecycleOwner, Observer { listOfAddedTools ->
            toolAdapter.submitList(listOfAddedTools)
        })
    }

    private fun deleteMachineFromDb(id: Long) {
        viewModelMainTools.deleteItemFromList(id)
    }

    private fun changeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
        val seekingTool = receivedTools.find { it.toolName == itemName }
        val maxValue: Int = seekingTool?.numberOfTools ?: GardenerApp.MAX_NUMBER_OF_MACHINES

        PickNumberDialog(
            currentValue,
            maxValue,
            object :
                PickNumberDialog.OnChosenNumberListener {
                override fun onChosenNumber(chosenNumber: Int) {
                    viewModelMainTools.updateNumberOfTools(chosenNumber, position)
                }
            }).show(childFragmentManager, null)
    }


    private fun changeFlagToOpposite(position: Int) {
        viewModelMainTools.reverseFlagOnTool(position)
    }

    companion object {
        fun create(gardenID: Long): ToolFragment {
            val fragment = ToolFragment()
            fragment.arguments = ToolViewModel.toBundle(gardenID)
            return fragment
        }

    }
}