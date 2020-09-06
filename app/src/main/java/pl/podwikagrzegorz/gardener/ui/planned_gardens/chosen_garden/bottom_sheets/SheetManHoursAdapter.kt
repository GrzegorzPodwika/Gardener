package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.BottomSheetSingleItemBinding

class SheetManHoursAdapter(
    private val workersFullNames: List<String>
) : RecyclerView.Adapter<SheetManHoursAdapter.ManHoursHolder>() {

    val listOfWorkedHours = MutableList(workersFullNames.size) {0.0}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ManHoursHolder {
        return ManHoursHolder.from(parent)
    }

    override fun getItemCount(): Int = workersFullNames.size

    override fun onBindViewHolder(holder: ManHoursHolder, position: Int) {
        holder.bind(workersFullNames[position])
        holder.binding.editTextNumbOfManHours.addTextChangedListener { text: Editable? ->
            text?.let {
                if (it.toString().isNotEmpty())
                    listOfWorkedHours[position] = it.toString().toDouble()
                else
                    listOfWorkedHours[position] = 0.0
            }
        }
    }

    class ManHoursHolder private constructor(val binding: BottomSheetSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fullName: String) {
            binding.textViewWorkerName.text = fullName
        }

        companion object {
            fun from(parent: ViewGroup) : ManHoursHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BottomSheetSingleItemBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
                return ManHoursHolder(binding)
            }
        }
    }

}