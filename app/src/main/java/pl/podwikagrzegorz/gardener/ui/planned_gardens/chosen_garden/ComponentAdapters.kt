package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.data.realm.ToolRealm
import pl.podwikagrzegorz.gardener.databinding.McvAddedToolBinding
import pl.podwikagrzegorz.gardener.databinding.McvToolFromDbBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

// 3 adapters for received data from Tool, Machine, Property values
class ReceivedToolsAdapter(
    private val itemRealmResults: RealmResults<ToolRealm>,
    private val listener: OnPushItemListener
) : RecyclerView.Adapter<ReceivedToolsAdapter.ReceivedToolsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedToolsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: McvToolFromDbBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mcv_tool_from_db, parent, false)

        return ReceivedToolsHolder(binding)
    }

    override fun getItemCount(): Int = itemRealmResults.size

    override fun onBindViewHolder(holder: ReceivedToolsHolder, position: Int) {
        holder.bind(itemRealmResults[position])
        holder.binding.imageButtonToolToPass.setOnClickListener {
            val id = itemRealmResults[position]?.id
            listener.onPushItemClick(id)
        }
    }

    class ReceivedToolsHolder(val binding: McvToolFromDbBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toolRealm: ToolRealm?) {
            binding.textViewToolFromDb.text = toolRealm?.toolName
        }
    }
}

class ReceivedMachinesAdapter(
    private val itemRealmResults: RealmResults<MachineRealm>,
    private val listener: OnPushItemListener
) : RecyclerView.Adapter<ReceivedMachinesAdapter.ReceivedMachinesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedMachinesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: McvToolFromDbBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mcv_tool_from_db, parent, false)

        return ReceivedMachinesHolder(binding)
    }

    override fun getItemCount(): Int = itemRealmResults.size

    override fun onBindViewHolder(holder: ReceivedMachinesHolder, position: Int) {
        holder.bind(itemRealmResults[position])
        holder.binding.imageButtonToolToPass.setOnClickListener {
            val id = itemRealmResults[position]?.id
            listener.onPushItemClick(id)
        }
    }

    class ReceivedMachinesHolder(val binding: McvToolFromDbBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(machineRealm: MachineRealm?) {
            binding.textViewToolFromDb.text = machineRealm?.machineName
        }
    }
}

class ReceivedPropertiesAdapter(
    private val itemRealmResults: RealmResults<PropertyRealm>,
    private val listener: OnPushItemListener
) : RecyclerView.Adapter<ReceivedPropertiesAdapter.ReceivedPropertiesHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivedPropertiesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: McvToolFromDbBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mcv_tool_from_db, parent, false)

        return ReceivedPropertiesHolder(binding)
    }

    override fun getItemCount(): Int = itemRealmResults.size

    override fun onBindViewHolder(holder: ReceivedPropertiesHolder, position: Int) {
        holder.bind(itemRealmResults[position])
        holder.binding.imageButtonToolToPass.setOnClickListener {
            val id = itemRealmResults[position]?.id
            listener.onPushItemClick(id)
        }
    }

    class ReceivedPropertiesHolder(val binding: McvToolFromDbBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(propertyRealm: PropertyRealm?) {
            binding.textViewToolFromDb.text = propertyRealm?.propertyName
        }
    }
}

//TODO zobaczyc czy dziala
//1 adapter for added data : Tool, Machine and Property values separately
class AddedItemAdapter(
    private val listOfItems: RealmList<ItemRealm>,
    private val listener: OnDeleteItemListener
) : RecyclerView.Adapter<AddedItemAdapter.AddedItemHolder>() {

    private val _listOfActiveTools: MutableList<Boolean> = mutableListOf()
    val listOfActiveTools : List<Boolean>
        get() = _listOfActiveTools

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: McvAddedToolBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mcv_added_tool, parent, false)

        return AddedItemHolder(binding)
    }

    override fun getItemCount(): Int = listOfItems.size

    override fun onBindViewHolder(holder: AddedItemHolder, position: Int) {
        val item = listOfItems[position]
        holder.bind(item, _listOfActiveTools[position])

        holder.binding.textViewNumbOfTools.setOnClickListener {
            listener.onChangeNumberOfItems(item?.numberOfItems ?: 0, position, item?.itemName ?: "")
        }

        holder.binding.imageButtonToolToDelete.setOnClickListener {
            listener.onDeleteItemClick(position.toLong())
        }

        holder.binding.textViewAddedTool.setOnClickListener {
            _listOfActiveTools[position] = !_listOfActiveTools[position]
            holder.checkIfTextViewShouldBeCrossed(_listOfActiveTools[position])
        }
    }


    class AddedItemHolder(val binding: McvAddedToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: ItemRealm?, isActive: Boolean) {
            tool?.let {
                binding.textViewAddedTool.text = it.itemName
                binding.textViewNumbOfTools.text = it.numberOfItems.toString()

                checkIfTextViewShouldBeCrossed(isActive)
            }
        }

        fun checkIfTextViewShouldBeCrossed(isActive: Boolean) {
            if (isActive) {
                binding.mcvAddedTool.foreground = null
                binding.mcvAddedTool.foreground = defaultMCVForeground
                enableButtonAndNumberPicker()
            } else {
                binding.mcvAddedTool.foreground = strikeThroughForeground
                disableButtonAndNumberPicker()
            }
        }

        private fun enableButtonAndNumberPicker() {
            binding.textViewNumbOfTools.isEnabled = true
            binding.imageButtonToolToDelete.isEnabled = true
        }

        private fun disableButtonAndNumberPicker() {
            binding.textViewNumbOfTools.isEnabled = false
            binding.imageButtonToolToDelete.isEnabled = false
        }

    }

    init {
        for (item in listOfItems) {
            _listOfActiveTools.add(item.isActive)
        }
    }

    companion object {
        private val strikeThroughForeground =
            GardenerApp.res.getDrawable(R.drawable.stroke_foreground, null)
        private val defaultMCVForeground =
            GardenerApp.res.getDrawable(R.drawable.mcv_foreground, null)

    }
}
