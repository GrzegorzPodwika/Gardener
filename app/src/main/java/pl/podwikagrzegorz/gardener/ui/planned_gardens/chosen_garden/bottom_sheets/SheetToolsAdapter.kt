package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.BottomSheetSingleItemBinding
import pl.podwikagrzegorz.gardener.databinding.BottomSheetToolCheckboxBinding
import pl.podwikagrzegorz.gardener.databinding.BottomSheetWorkerCheckboxBinding
import pl.podwikagrzegorz.gardener.databinding.McvAddedToolBinding

class SheetToolsAdapter(
    private val listOfToolNames: List<String>
) : RecyclerView.Adapter<SheetToolsAdapter.ReceivedToolsHolder>() {

    private val _listOfCheckedTools = mutableListOf<Boolean>()
    val listOfCheckedTools: List<Boolean>
        get() = _listOfCheckedTools

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedToolsHolder {
        return ReceivedToolsHolder.from(parent)
    }

    override fun getItemCount(): Int = listOfToolNames.size

    override fun onBindViewHolder(holder: ReceivedToolsHolder, position: Int) {
        holder.bind(listOfToolNames[position])
        holder.binding.materialCheckBoxName.setOnCheckedChangeListener { _, isChecked ->
            _listOfCheckedTools[position] = isChecked
        }
    }

    class ReceivedToolsHolder private constructor(val binding: BottomSheetToolCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toolName: String) {
            binding.materialCheckBoxName.text = toolName
        }

        companion object {
            fun from(parent: ViewGroup): ReceivedToolsHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BottomSheetToolCheckboxBinding.inflate(layoutInflater, parent, false)

                return ReceivedToolsHolder(binding)
            }
        }
    }

    init {
        listOfToolNames.forEach { _ -> _listOfCheckedTools.add(false) }
    }
}