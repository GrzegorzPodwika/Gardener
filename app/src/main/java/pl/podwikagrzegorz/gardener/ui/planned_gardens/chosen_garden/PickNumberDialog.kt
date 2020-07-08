package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.DialogPickNumberBinding

class PickNumberDialog(
    private val currentValue: Int,
    private val maxValue: Int,
    private val listener: OnChosenNumberListener
) : DialogFragment() {
    private lateinit var binding: DialogPickNumberBinding

    interface OnChosenNumberListener {
        fun onChosenNumber(chosenNumber: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_pick_number, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presetNumberPicker()
        setOnPickNumberButtonListener()
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