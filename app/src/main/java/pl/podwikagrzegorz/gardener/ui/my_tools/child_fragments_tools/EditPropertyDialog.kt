package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.databinding.DialogEditPropertyBinding

class EditPropertyDialog(
    private val propertyToEdit: Property,
    private val listener: OnChangedPropertyListener
) : DialogFragment(){
    private lateinit var binding: DialogEditPropertyBinding

    interface OnChangedPropertyListener {
        fun onChangedProperty(newProperty: Property)
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
        binding.editTextPropertyName.setText(propertyToEdit.propertyName)
        binding.editTextAmountOfProperties.setText(propertyToEdit.amountOfProperties)
    }

    private fun setUpListenersToButtons() {
        binding.materialButtonCancelEditingItem.setOnClickListener {
            dismiss()
        }

        binding.materialButtonConfirmEditingItem.setOnClickListener {
            val newItemName = if(binding.editTextPropertyName.text.isNotEmpty()) {
                binding.editTextPropertyName.text.toString()
            } else {
                propertyToEdit.propertyName
            }

            val newNumberOfItems = if(binding.editTextAmountOfProperties.text.isNotEmpty()) {
                binding.editTextAmountOfProperties.text.toString()
            } else {
                "0"
            }
            val newProperty = Property(newItemName, newNumberOfItems, propertyToEdit.timestamp, propertyToEdit.documentId)
            listener.onChangedProperty(newProperty)
            dismiss()
        }
    }

}