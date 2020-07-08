package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.BottomSheetManHoursBinding
import pl.podwikagrzegorz.gardener.extensions.getDate
import java.util.*

class SheetManHoursFragment(
    workersFullNames: List<String>,
    private val listener: OnGetListOfWorkedHoursWithPickedDate

) : BottomSheetDialogFragment() {
    private lateinit var binding: BottomSheetManHoursBinding
    private val adapter =
        SheetManHoursAdapter(
            workersFullNames
        )

    interface OnGetListOfWorkedHoursWithPickedDate {
        fun onGetListOfWorkedHoursWithPickedDate(listOfWorkedHours: List<Double>, date: Date)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_man_hours, container, false)
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

        setRecViewWithCurrentlyWorking()
        setAddWorkedHoursButton()
    }

    private fun setRecViewWithCurrentlyWorking() {
        binding.recyclerViewManHoursList.adapter = adapter
    }

    private fun setAddWorkedHoursButton() {
        binding.materialButtonConfirmAddingManHours.setOnClickListener {
            val listOfWorkedHours = adapter.listOfWorkedHours
            val pickedDate = binding.datePickerDayOfWork.getDate()

            listener.onGetListOfWorkedHoursWithPickedDate(listOfWorkedHours, pickedDate)

            dismiss()
        }
    }
}