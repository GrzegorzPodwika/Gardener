package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.databinding.BottomSheetToolCheckboxBinding

class SheetToolsAdapter(
    private val listOfTools: List<Tool>
) : RecyclerView.Adapter<SheetToolsAdapter.ReceivedToolsHolder>() {

    private val _listOfCheckedTools = MutableList(listOfTools.size) { false }
    val listOfCheckedTools: List<Boolean>
        get() = _listOfCheckedTools

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedToolsHolder {
        return ReceivedToolsHolder.from(parent)
    }

    override fun getItemCount(): Int = listOfTools.size

    override fun onBindViewHolder(holder: ReceivedToolsHolder, position: Int) {
        holder.bind(listOfTools[position])
        holder.binding.materialCheckBoxName.setOnCheckedChangeListener { _, isChecked ->
            _listOfCheckedTools[position] = isChecked
        }
    }

    class ReceivedToolsHolder private constructor(val binding: BottomSheetToolCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Tool) {
            binding.materialCheckBoxName.text = tool.toolName
        }

        companion object {
            fun from(parent: ViewGroup): ReceivedToolsHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BottomSheetToolCheckboxBinding.inflate(layoutInflater, parent, false)

                return ReceivedToolsHolder(binding)
            }
        }
    }

}