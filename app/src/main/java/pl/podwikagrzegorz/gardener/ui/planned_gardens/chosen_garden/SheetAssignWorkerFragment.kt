package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

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
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.databinding.BottomSheetAssignWorkerBinding

class SheetAssignWorkerFragment(
    private val workersList: RealmResults<WorkerRealm>,
    private val listener: OnGetListOfWorkersFullNameListener
) :
    BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetAssignWorkerBinding
    private val adapter = SheetAssignWorkerAdapter(workersList)

    interface OnGetListOfWorkersFullNameListener {
        fun onGetListOfWorkersFullName(listOfWorkersFullName: List<String>)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_assign_worker, container, false)
        return binding.root
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setRecViewWithReceivedWorkers()
        setAddWorkersButton()
    }

    private fun setRecViewWithReceivedWorkers() {
        binding.recyclerViewReceivedWorkers.layoutManager = LinearLayoutManager(requireContext())
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