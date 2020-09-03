package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.ToolsChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.ToolViewModel

//Class No4 - Tools
@AndroidEntryPoint
class ToolFragment : Fragment() {
    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val viewModelMainTools: ToolViewModel by viewModels()
    private lateinit var toolAdapter: AddedItemAdapter

    private val receivedToolsViewModel: ToolsChildViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolsInViewpagerBinding.inflate(inflater, container, false)
        receivedToolsViewModel.preInitialize()

        connectRecyclerViewWithQuery()
        setUpBinding()
        setOnAddToolsButtonListener()

        return binding.root
    }



    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Item>()
            .setQuery(viewModelMainTools.getTakenToolsQuerySortedByTimestamp(), Item::class.java)
            .setLifecycleOwner(this)
            .build()

        toolAdapter = AddedItemAdapter(options, object : OnClickItemListener {
            override fun onChangeFlagToOpposite(documentId: String) {
                viewModelMainTools.reverseFlagOnTool(childDocumentId = documentId)
            }

            override fun onClickItem(documentId: String) {
                viewModelMainTools.deleteItemFromList(childDocumentId = documentId)
            }
        })
    }

    private fun setUpBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerViewAddedTools.adapter = toolAdapter
        }
    }

    private fun setOnAddToolsButtonListener() {
        binding.materialButtonAddTools.setOnClickListener {
            SheetToolsFragment(
                receivedToolsViewModel.listOfTools,
                object : SheetToolsFragment.OnGetListOfPickedItemsListener {

                    override fun onGetListOfPickedItems(listOfPickedItems: List<Item>) {
                        viewModelMainTools.addListOfPickedTools(listOfPickedItems)
                    }

                }
            ).show(childFragmentManager, null)
        }
    }

    companion object {
        fun create(gardenTitle: String): ToolFragment {
            val fragment = ToolFragment()
            fragment.arguments = toBundle(gardenTitle)
            return fragment
        }

    }
}




/*       private fun addListOfPickedToolsToMainList(listOfPickedItems: List<Boolean>) {
        val listOfItemRealm = mutableListOf<Item>()
        val receivedTools = receivedToolsViewModel.listOfTools

        for (index in listOfPickedItems.indices) {
            if (listOfPickedItems[index]) {
                val toolToAdd = Item(receivedTools[index].toolName, receivedTools[index].numberOfTools)
                listOfItemRealm.add(toolToAdd)
            }
        }

        viewModelMainTools.addListOfPickedTools(listOfItemRealm)
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
    }*/
