package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.databinding.BottomSheetAssignWorkerBinding

class SheetToolsFragment(
    private val listOfTools: List<Tool>,
    private val listener : OnGetListOfPickedItemsListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAssignWorkerBinding
    private val adapter = SheetToolsAdapter(listOfTools)

    fun interface OnGetListOfPickedItemsListener {
        fun onGetListOfPickedItems(listOfPickedItems: List<Item>)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        bottomSheetDialog.setOnShowListener {
            val frameLayout =
                bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            if (frameLayout != null) {
                val behavior: BottomSheetBehavior<FrameLayout> =
                    BottomSheetBehavior.from(frameLayout)
                behavior.skipCollapsed = true
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetAssignWorkerBinding.inflate(inflater, container, false)

        setRecViewWithReceivedTools()
        setAddToolsButtonListener()

        return binding.root
    }

    private fun setRecViewWithReceivedTools() {
        binding.recyclerViewReceivedWorkers.adapter = adapter
    }

    private fun setAddToolsButtonListener() {
        binding.materialButtonConfirmAddingWorkers.setOnClickListener {
            val listOfCheckedTools = adapter.listOfCheckedTools
            val listOfPickedItems = mutableListOf<Item>()

            for (index in listOfCheckedTools.indices) {
                if (listOfCheckedTools[index]) {
                    val tool = listOfTools[index]
                    val item = Item(tool.toolName, tool.numberOfTools, tool.numberOfTools)
                    listOfPickedItems.add(item)
                }
            }
            listener.onGetListOfPickedItems(listOfPickedItems)
            dismiss()
        }
    }

}