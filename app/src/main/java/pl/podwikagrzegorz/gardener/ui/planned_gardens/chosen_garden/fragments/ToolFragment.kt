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
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.ToolRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.ToolsChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.ToolViewModel
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

//Class No4 - Tools
class ToolFragment : Fragment(), OnDeleteItemListener {
    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val gardenID: Long by lazy {
        ToolViewModel.fromBundle(requireArguments())
    }
    private val viewModelMainTools: ToolViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private val viewModelReceivedTools: ToolsChildViewModel by lazy {
        ViewModelProvider(this).get(ToolsChildViewModel::class.java)
    }

    private val receivedTools: RealmResults<ToolRealm> by lazy {
        viewModelReceivedTools.getListOfToolsAsRealmResults()
    }

    private val receivedToolNames = mutableListOf<String>()
    private lateinit var adapter: AddedItemAdapter

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
        setOnAddToolsButtonListener()
        mapReceivedToolsIntoNames()
        observeListOfAddedTools()
    }

    private fun presetRecyclerView() {
        binding.recyclerViewAddedTools.layoutManager =
            LinearLayoutManager(requireContext())
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
                receivedTools[index]?.let {
                    val toolToAdd = ItemRealm(it.toolName, it.numberOfTools)
                    listOfItemRealm.add(toolToAdd)
                }
            }
        }

        viewModelMainTools.addListOfPickedTools(listOfItemRealm)
    }

    private fun mapReceivedToolsIntoNames() {
        receivedTools.forEach { receivedToolNames.add(it.toolName) }
    }

    private fun observeListOfAddedTools() {
        viewModelMainTools.listOfTools
            .observe(viewLifecycleOwner, Observer {
                adapter = AddedItemAdapter(it, this)
                binding.recyclerViewAddedTools.adapter = adapter
            })
    }

    override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
        val maxValue: Int = viewModelReceivedTools.findMaxValueOf(itemName)

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

    override fun onDeleteItemClick(id: Long?) {
        viewModelMainTools.deleteItemFromList(id)
    }

    override fun onChangeFlagToOpposite(position: Int) {
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