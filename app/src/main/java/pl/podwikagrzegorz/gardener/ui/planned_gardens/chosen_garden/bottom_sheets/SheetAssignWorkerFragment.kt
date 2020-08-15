package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.databinding.BottomSheetAssignWorkerBinding

class SheetAssignWorkerFragment(
    private val workersList: List<Worker>,
    private val listener: OnGetListOfWorkersFullNameListener
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAssignWorkerBinding
    private val adapter = SheetAssignWorkerAdapter()

    interface OnGetListOfWorkersFullNameListener {
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
        setRecViewWithReceivedWorkers()
        setAddWorkersButton()

        return binding.root
    }

    private fun setUpBinding() {
        binding.apply {
            lifecycleOwner = this@SheetAssignWorkerFragment
        }
    }

    private fun setRecViewWithReceivedWorkers() {
        adapter.initAndSubmitList(workersList)
        binding.recyclerViewReceivedWorkers.adapter = adapter
    }

    private fun setAddWorkersButton() {
        binding.materialButtonConfirmAddingWorkers.setOnClickListener {
            val listOfWorkersFullName = adapter.getListOfWorkersFullName()
            listener.onGetListOfWorkersFullName(listOfWorkersFullName)
            dismiss()
        }
    }
}