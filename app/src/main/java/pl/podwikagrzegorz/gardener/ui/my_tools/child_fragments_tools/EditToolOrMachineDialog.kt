package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pl.podwikagrzegorz.gardener.databinding.DialogEditItemBinding

class EditToolOrMachineDialog(
    private val itemName: String,
    private val numberOfItems: Int,
    private val listener: OnChangedItemAttributesListener
) : DialogFragment() {
    private lateinit var binding: DialogEditItemBinding

    interface OnChangedItemAttributesListener {
        fun onChangedItemAttributes(newItemName: String, newNumberOfItems: Int)
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
        binding = DialogEditItemBinding.inflate(inflater, container, false)

        setUpEditTextsWithCurrentVariables()
        setUpListenersToButtons()

        return binding.root
    }

    private fun setUpEditTextsWithCurrentVariables() {
        binding.editTextItemName.setText(itemName)
        binding.editTextNumberOfItems.setText(numberOfItems.toString())
    }

    private fun setUpListenersToButtons() {
        binding.materialButtonCancelEditingItem.setOnClickListener {
            dismiss()
        }

        binding.materialButtonConfirmEditingItem.setOnClickListener {
            val newItemName = if(binding.editTextItemName.text.isNotEmpty()) {
                binding.editTextItemName.text.toString()
            } else {
                itemName
            }

            val newNumberOfItems = if(binding.editTextNumberOfItems.text.isNotEmpty()) {
                binding.editTextNumberOfItems.text.toString().toInt()
            } else {
                0
            }

            listener.onChangedItemAttributes(newItemName, newNumberOfItems)
            dismiss()
        }
    }
}