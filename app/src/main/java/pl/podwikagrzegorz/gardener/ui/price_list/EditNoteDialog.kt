package pl.podwikagrzegorz.gardener.ui.price_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.data.domain.Note
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.databinding.DialogEditPropertyBinding

class EditNoteDialog(
    private val noteToEdit: Note,
    private val listener: OnChangedNoteListener
) : DialogFragment() {

    private lateinit var binding: DialogEditPropertyBinding

    interface OnChangedNoteListener {
        fun onChangedNote(updatedNote: Note)
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
        binding = DialogEditPropertyBinding.inflate(inflater, container, false)

        setUpEditTextsWithCurrentVariables()
        setUpListenersToButtons()

        return binding.root
    }

    private fun setUpEditTextsWithCurrentVariables() {
        binding.editTextPropertyName.setText(noteToEdit.service)
        binding.editTextAmountOfProperties.setText(noteToEdit.priceOfService)
    }

    private fun setUpListenersToButtons() {
        binding.materialButtonCancelEditingItem.setOnClickListener {
            dismiss()
        }

        binding.materialButtonConfirmEditingItem.setOnClickListener {
            val newService = if (binding.editTextPropertyName.text.isNotEmpty()) {
                binding.editTextPropertyName.text.toString()
            } else {
                noteToEdit.service
            }

            val newPriceOfService = if (binding.editTextAmountOfProperties.text.isNotEmpty()) {
                binding.editTextAmountOfProperties.text.toString()
            } else {
                "0"
            }
            val updatedNote =
                Note(newService, newPriceOfService, noteToEdit.timestamp, noteToEdit.documentId)
            listener.onChangedNote(updatedNote)
            dismiss()
        }
    }
}

