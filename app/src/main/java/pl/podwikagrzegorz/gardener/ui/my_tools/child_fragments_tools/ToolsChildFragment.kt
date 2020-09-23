package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.databinding.FragmentChildToolsBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

@AndroidEntryPoint
class ToolsChildFragment : Fragment() {

    private lateinit var binding: FragmentChildToolsBinding
    private val viewModel: ToolsChildViewModel by viewModels()
    private lateinit var toolAdapter: ToolAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildToolsBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpBindingWithViewModel()
        observeAddToolButton()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Tool>()
            .setQuery(viewModel.getQuerySortedByTimestamp(), Tool::class.java)
            .setLifecycleOwner(this)
            .build()

        toolAdapter = ToolAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModel.deleteTool(documentId)
            }
        }, object : OnEditItemListener<Tool> {
            override fun onEditItem(itemToEdit: Tool) {
                showEditItemDialog(itemToEdit)
            }
        })
    }

    private fun showEditItemDialog(toolToEdit: Tool) {
        EditToolOrMachineDialog(toolToEdit.toolName, toolToEdit.numberOfTools, object : EditToolOrMachineDialog.OnChangedItemAttributesListener {
            override fun onChangedItemAttributes(newItemName: String, newNumberOfItems: Int) {
                viewModel.updateTool(toolToEdit.documentId, newItemName, newNumberOfItems)
            }
        }).show(childFragmentManager, null)
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            toolsChildViewModel = viewModel
            recyclerViewMyTools.adapter = toolAdapter
            toolName = ""
            numbOfToolsAsString = ""
        }
    }

    private fun observeAddToolButton() {
        viewModel.eventAddTool.observe(viewLifecycleOwner, { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })

        viewModel.errorEditTextEmpty.observe(viewLifecycleOwner, { isEmpty ->
            if (isEmpty) {
                showErrorToast()
            }
        })

    }

    private fun showErrorToast() {
        toast(getString(R.string.fill_up_field))
        viewModel.onErrorShowComplete()
    }

    private fun cleanUp() {
        clearViews()
        setFocusOnFirstEditText()
        viewModel.onAddToolComplete()
    }

    private fun clearViews() {
        binding.editTextToolNameAdd.text = null
        binding.editTextPriceOfTool.text = null
    }

    private fun setFocusOnFirstEditText() {
        binding.editTextToolNameAdd.requestFocus()
    }

}
