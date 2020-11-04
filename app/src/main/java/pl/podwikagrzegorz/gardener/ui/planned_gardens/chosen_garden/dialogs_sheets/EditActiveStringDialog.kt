package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.data.domain.ActiveString
import pl.podwikagrzegorz.gardener.databinding.DialogEditActiveStringBinding

class EditActiveStringDialog(
    private val activeStringToEdit: ActiveString,
    private val listener: OnChangedActiveStringListener
) : DialogFragment() {
    private lateinit var binding: DialogEditActiveStringBinding

    fun interface OnChangedActiveStringListener {
        fun onChangedActiveString(newActiveString: ActiveString)
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
        binding = DialogEditActiveStringBinding.inflate(inflater, container, false)

        setUpEditTextsWithCurrentVariables()
        setUpListenersToButtons()

        return binding.root
    }

    private fun setUpEditTextsWithCurrentVariables() {
        binding.editTextActiveString.setText(activeStringToEdit.name)
    }

    private fun setUpListenersToButtons() {
        binding.materialButtonCancelEditingItem.setOnClickListener {
            dismiss()
        }

        binding.materialButtonConfirmEditingItem.setOnClickListener {
            val newItemName = if(binding.editTextActiveString.text.isNotEmpty()) {
                binding.editTextActiveString.text.toString()
            } else {
                activeStringToEdit.name
            }

            val newActiveString = ActiveString(newItemName, activeStringToEdit.isActive, activeStringToEdit.timestamp, activeStringToEdit.documentId)
            listener.onChangedActiveString(newActiveString)
            dismiss()
        }
    }

}