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
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.SheetToolsFragment
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
            .setQuery(viewModelMainTools.getTakenToolsQuerySortedByActivity(), Item::class.java)
            .setLifecycleOwner(this)
            .build()

        toolAdapter = AddedItemAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModelMainTools.deleteItemFromList(childDocumentId = documentId)
            }

            override fun onChangeFlagToOpposite(documentId: String, isActive: Boolean) {
                viewModelMainTools.reverseFlagOnTool(childDocumentId = documentId, isActive)
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

    private fun showPickNumberDialog(documentId: String, currentNumberOfItems: Int, maxNumberOfItems: Int) {
        PickNumberDialog(currentNumberOfItems, maxNumberOfItems
        ) { chosenNumber ->
            viewModelMainTools.updateNumberOfTools(
                childDocumentId = documentId,
                chosenNumber
            )
        }.show(childFragmentManager, null)
    }

    private fun setUpBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerViewAddedTools.adapter = toolAdapter
        }
    }

    private fun setOnAddToolsButtonListener() {
        binding.materialButtonAddTools.setOnClickListener {
            SheetToolsFragment(receivedToolsViewModel.listOfTools) { listOfPickedItems ->
                viewModelMainTools.addListOfPickedTools(listOfPickedItems)
            }.show(childFragmentManager, null)
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