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
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.databinding.BottomSheetAssignWorkerBinding

class SheetAssignWorkerFragment(
    private val workersList: List<Worker>,
    private val listener: OnGetListOfWorkersFullNameListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAssignWorkerBinding
    private val workerAdapter = SheetAssignWorkerAdapter(workersList)

    fun interface OnGetListOfWorkersFullNameListener {
        fun onGetListOfWorkersFullName(listOfWorkersFullName: List<String>)
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

        setUpBinding()
        setAddWorkersButton()

        return binding.root
    }

    private fun setUpBinding() {
        binding.apply {
            lifecycleOwner = this@SheetAssignWorkerFragment
            recyclerViewReceivedWorkers.adapter = workerAdapter
        }
    }

    private fun setAddWorkersButton() {
        binding.materialButtonConfirmAddingWorkers.setOnClickListener {
            val listOfWorkersFullName = workerAdapter.getListOfWorkersFullName()
            listener.onGetListOfWorkersFullName(listOfWorkersFullName)
            dismiss()
        }
    }
}