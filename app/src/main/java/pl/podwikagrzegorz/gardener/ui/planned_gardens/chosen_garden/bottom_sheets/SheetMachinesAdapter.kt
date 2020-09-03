package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.databinding.BottomSheetToolCheckboxBinding

class SheetMachinesAdapter(
    private val listOfMachines: List<Machine>
) : RecyclerView.Adapter<SheetMachinesAdapter.ReceivedMachinesHolder>() {

    private val _listOfCheckedMachines = MutableList(listOfMachines.size) { false }
    val listOfCheckedMachines: List<Boolean>
        get() = _listOfCheckedMachines

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedMachinesHolder {
        return ReceivedMachinesHolder.from(parent)
    }

    override fun getItemCount(): Int = listOfMachines.size

    override fun onBindViewHolder(holder: ReceivedMachinesHolder, position: Int) {
        holder.bind(listOfMachines[position])
        holder.binding.materialCheckBoxName.setOnCheckedChangeListener { _, isChecked ->
            _listOfCheckedMachines[position] = isChecked
        }
    }

    class ReceivedMachinesHolder private constructor(val binding: BottomSheetToolCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Machine) {
            binding.materialCheckBoxName.text = tool.machineName
        }

        companion object {
            fun from(parent: ViewGroup): ReceivedMachinesHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BottomSheetToolCheckboxBinding.inflate(layoutInflater, parent, false)

                return ReceivedMachinesHolder(binding)
            }
        }
    }


}