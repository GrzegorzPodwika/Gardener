package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.databinding.McvSingleMachineBinding
import pl.podwikagrzegorz.gardener.databinding.McvSinglePropertyBinding
import pl.podwikagrzegorz.gardener.databinding.McvSingleToolBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class ToolAdapter(
    private val listener: OnClickItemListener
) : ListAdapter<Tool, ToolAdapter.ToolHolder>(ToolDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolHolder {
        return ToolHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ToolHolder, position: Int) {
        val tool = getItem(position)
        holder.bind(tool, listener)
    }

    class ToolHolder private constructor(private val binding: McvSingleToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Tool, listener: OnClickItemListener) {
            binding.tool = tool
            binding.onCLickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ToolHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvSingleToolBinding.inflate(layoutInflater, parent, false)
                return ToolHolder(binding)
            }
        }

    }

    object ToolDiffCallback : DiffUtil.ItemCallback<Tool>() {
        override fun areItemsTheSame(oldItem: Tool, newItem: Tool): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Tool, newItem: Tool): Boolean {
            return oldItem == newItem
        }
    }
}




class MachineAdapter(
    private val listener: OnClickItemListener
) : ListAdapter<Machine, MachineAdapter.MachineHolder>(MachineDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineHolder {
        return MachineHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MachineHolder, position: Int) {
        val machine = getItem(position)
        holder.bind(machine, listener)
    }

    class MachineHolder private constructor(private val binding: McvSingleMachineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(machine: Machine, listener: OnClickItemListener) {
            binding.machine = machine
            binding.onClickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MachineHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvSingleMachineBinding.inflate(layoutInflater, parent, false)
                return MachineHolder(binding)
            }
        }

    }

    object MachineDiffCallback : DiffUtil.ItemCallback<Machine>() {
        override fun areItemsTheSame(oldItem: Machine, newItem: Machine): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Machine, newItem: Machine): Boolean {
            return oldItem == newItem
        }
    }
}



class PropertyAdapter(
    private val listener: OnClickItemListener
) : ListAdapter<Property, PropertyAdapter.PropertyHolder>(PropertyDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PropertyHolder {
        return PropertyHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PropertyHolder, position: Int) {
        val property = getItem(position)
        holder.bind(property, listener)
    }

    class PropertyHolder private constructor(private val binding: McvSinglePropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property, listener: OnClickItemListener) {
            binding.property = property
            binding.onClickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): PropertyHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvSinglePropertyBinding.inflate(layoutInflater, parent, false)
                return PropertyHolder(binding)
            }
        }
    }

    object PropertyDiffCallback : DiffUtil.ItemCallback<Property>() {
        override fun areItemsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Property, newItem: Property): Boolean {
            return oldItem == newItem
        }
    }
}

