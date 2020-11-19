package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.data.domain.ManHours
import pl.podwikagrzegorz.gardener.databinding.DialogEditManHoursBinding
import pl.podwikagrzegorz.gardener.extensions.toSimpleFormat
import timber.log.Timber

class EditManHoursDialog(
    private val manHoursToEdit: ManHours,
    private val editManHoursListener: EditManHoursListener
) : DialogFragment(){
    private lateinit var binding: DialogEditManHoursBinding

    fun interface EditManHoursListener {
        fun onChangedManHours(updatedManHours: ManHours)
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        dialog?.apply {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            window?.setLayout(width, height)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogEditManHoursBinding.inflate(inflater, container, false)

        setUpViewsWithCurrentValues()
        setUpButtonsListeners()

        return binding.root
    }

    private fun setUpViewsWithCurrentValues() {
        binding.apply {
            textViewManhoursDate.text = manHoursToEdit.date.toSimpleFormat()
            editTextNumberOfHours.setText(manHoursToEdit.numberOfWorkedHours.toString())
        }
    }

    private fun setUpButtonsListeners() {
        binding.materialButtonCancelEditingHours.setOnClickListener {
            dismiss()
        }

        binding.materialButtonConfirmEditingHours.setOnClickListener {
            val newValue: Double = if(!binding.editTextNumberOfHours.text.isNullOrEmpty()) {
                try {
                    binding.editTextNumberOfHours.text.toString().toDouble()
                } catch (e: NumberFormatException) {
                    Timber.e(e)
                    manHoursToEdit.numberOfWorkedHours
                }
            } else {
                manHoursToEdit.numberOfWorkedHours
            }

            editManHoursListener.onChangedManHours(ManHours(manHoursToEdit.date, newValue))

            dismiss()
        }
    }
}