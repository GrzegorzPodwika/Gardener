package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pl.podwikagrzegorz.gardener.data.domain.ActiveProperty
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.databinding.BottomSheetAssignWorkerBinding

class SheetPropertiesFragment(
    private val listOfProperties: List<Property>,
    private val listener : OnGetListOfPickedPropertiesListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAssignWorkerBinding
    private val adapter = SheetPropertiesAdapter(listOfProperties)

    interface OnGetListOfPickedPropertiesListener {
        fun onGetListOfPickedItems(listOfPickedItems: List<ActiveProperty>)
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
            val listOfCheckedTools = adapter.listOfCheckedProperties
            val listOfPickedItems = mutableListOf<ActiveProperty>()

            for (index in listOfCheckedTools.indices) {
                if (listOfCheckedTools[index]) {
                    val property = listOfProperties[index]
                    listOfPickedItems.add(ActiveProperty(property.propertyName, property.amountOfProperties))
                }
            }
            listener.onGetListOfPickedItems(listOfPickedItems)
            dismiss()
        }
    }

}