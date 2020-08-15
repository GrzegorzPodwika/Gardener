package pl.podwikagrzegorz.gardener.ui.planned_gardens.garden_to_add

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

@Deprecated("Replaced by MaterialDatePicker")
class DatePickerFragment(
    private val isRequireStartDate: Boolean,
    private val listener: OnDateSelectedListener
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface OnDateSelectedListener {
        fun onDateSelected(year: Int, month: Int, dayOfMonth: Int, isRequireStartDate: Boolean)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

        return DatePickerDialog(requireContext(), this, year, month, dayOfMonth)
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.onDateSelected(year, month, dayOfMonth, isRequireStartDate)
    }
}