package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.databinding.BottomSheetToolCheckboxBinding

class SheetPropertiesAdapter(
    private val listOfProperties: List<Property>
) : RecyclerView.Adapter<SheetPropertiesAdapter.ReceivedPropertiesHolder>() {

    private val _listOfCheckedProperties = MutableList(listOfProperties.size) { false }
    val listOfCheckedProperties: List<Boolean>
        get() = _listOfCheckedProperties

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedPropertiesHolder {
        return ReceivedPropertiesHolder.from(parent)
    }

    override fun getItemCount(): Int = listOfProperties.size

    override fun onBindViewHolder(holder: ReceivedPropertiesHolder, position: Int) {
        holder.bind(listOfProperties[position])
        holder.binding.materialCheckBoxName.setOnCheckedChangeListener { _, isChecked ->
            _listOfCheckedProperties[position] = isChecked
        }
    }

    class ReceivedPropertiesHolder private constructor(val binding: BottomSheetToolCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property) {
            binding.materialCheckBoxName.text = property.propertyName
        }

        companion object {
            fun from(parent: ViewGroup): ReceivedPropertiesHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BottomSheetToolCheckboxBinding.inflate(layoutInflater, parent, false)

                return ReceivedPropertiesHolder(binding)
            }
        }
    }

}