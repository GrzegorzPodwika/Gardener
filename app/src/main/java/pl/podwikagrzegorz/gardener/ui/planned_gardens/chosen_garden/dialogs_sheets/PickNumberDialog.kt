package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.databinding.DialogPickNumberBinding

class PickNumberDialog(
    private val currentValue: Int,
    private val maxValue: Int,
    private val listener: OnChosenNumberListener
) : DialogFragment() {
    private lateinit var binding: DialogPickNumberBinding

    fun interface OnChosenNumberListener {
        fun onChosenNumber(chosenNumber: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPickNumberBinding.inflate(inflater, container, false)

        presetNumberPicker()
        setOnPickNumberButtonListener()

        return binding.root
    }

    private fun presetNumberPicker() {
        binding.numberPicker.minValue = 0
        binding.numberPicker.maxValue = maxValue
        binding.numberPicker.value = currentValue
    }

    private fun setOnPickNumberButtonListener() {
        binding.buttonPickNumber.setOnClickListener {
            val chosenNumber = binding.numberPicker.value
            listener.onChosenNumber(chosenNumber)
            dialog?.dismiss()
        }
    }
}