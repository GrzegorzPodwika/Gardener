package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.data.domain.Tool
import pl.podwikagrzegorz.gardener.databinding.McvSingleMachineBinding
import pl.podwikagrzegorz.gardener.databinding.McvSinglePropertyBinding
import pl.podwikagrzegorz.gardener.databinding.McvSingleToolBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.price_list.OnChangeItemListener

class ToolAdapter(
    options: FirestoreRecyclerOptions<Tool>,
    private val listener: OnClickItemListener,
    private val changeItemListener: OnChangeItemListener<Tool>
) : FirestoreRecyclerAdapter<Tool, ToolAdapter.ToolHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolHolder {
        return ToolHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ToolHolder, position: Int, model: Tool) {
        holder.bind(model, listener, changeItemListener)
    }

    class ToolHolder private constructor(private val binding: McvSingleToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: Tool, listener: OnClickItemListener, changeItemListener: OnChangeItemListener<Tool>) {
            binding.tool = tool
            binding.onCLickListener = listener
            binding.materialCardViewSingleTool.setOnLongClickListener {
                changeItemListener.onChangedItem(tool)
                true
            }
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
}




class MachineAdapter(
    options : FirestoreRecyclerOptions<Machine>,
    private val listener: OnClickItemListener,
    private val changeItemListener: OnChangeItemListener<Machine>
) : FirestoreRecyclerAdapter<Machine, MachineAdapter.MachineHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MachineHolder {
        return MachineHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MachineHolder, position: Int, model: Machine) {
        holder.bind(model, listener, changeItemListener)
    }

    class MachineHolder private constructor(private val binding: McvSingleMachineBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(machine: Machine, listener: OnClickItemListener, changeItemListener: OnChangeItemListener<Machine>) {
            binding.machine = machine
            binding.onClickListener = listener
            binding.materialCardViewSingleMachine.setOnLongClickListener {
                changeItemListener.onChangedItem(machine)
                true
            }
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

}

class PropertyAdapter(
    options: FirestoreRecyclerOptions<Property>,
    private val listener: OnClickItemListener,
    private val changeItemListener: OnChangeItemListener<Property>
) : FirestoreRecyclerAdapter<Property, PropertyAdapter.PropertyHolder>(options) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PropertyHolder {
        return PropertyHolder.from(parent)
    }

    override fun onBindViewHolder(holder: PropertyHolder, position: Int, model: Property) {
        holder.bind(model, listener, changeItemListener)
    }

    class PropertyHolder private constructor(private val binding: McvSinglePropertyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(property: Property, listener: OnClickItemListener, changeItemListener: OnChangeItemListener<Property>) {
            binding.property = property
            binding.onClickListener = listener
            binding.materialCardViewSingleProperty.setOnLongClickListener {
                changeItemListener.onChangedItem(property)
                true
            }
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

}

