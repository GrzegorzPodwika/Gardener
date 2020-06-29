package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmList
import io.realm.RealmResults
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
) : RecyclerView.Adapter<ReceivedPropertiesAdapter.ReceivedPropertiesHolder>(){

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
    class ReceivedPropertiesHolder(val binding: McvToolFromDbBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(propertyRealm: PropertyRealm?) {
            binding.textViewToolFromDb.text = propertyRealm?.propertyName
        }
    }
}

//1 adapter for added data : Tool, Machine and Property values separately
class AddedItemAdapter(
    private val listOfItems: RealmList<ItemRealm>,
    private val listener: OnDeleteItemListener
) : RecyclerView.Adapter<AddedItemAdapter.AddedItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedItemHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: McvAddedToolBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mcv_added_tool, parent, false)

        return AddedItemHolder(binding)
    }

    override fun getItemCount(): Int = listOfItems.size

    override fun onBindViewHolder(holder: AddedItemHolder, position: Int) {
        holder.bind(listOfItems[position])
        holder.binding.editTextNumbOfTools.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(text: Editable?) {
                val getText = text.toString()
                if (getText != "")
                    listener.onChangeNumberOfItems(text.toString().toInt(), position)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
        holder.binding.imageButtonToolToDelete.setOnClickListener {
            listener.onDeleteItemClick(position.toLong())
        }
    }

    class AddedItemHolder(val binding: McvAddedToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tool: ItemRealm?){
            binding.textViewAddedTool.text = tool?.itemName
            binding.editTextNumbOfTools.setText(tool?.numberOfItems.toString())
        }
    }


}

/*class PhotosAdapter(
    private val listOfPicturesPaths : RealmList<String>
) : RecyclerView.Adapter<PhotosAdapter.PhotosHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: PhotosHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class PhotosHolder() : RecyclerView.ViewHolder() {

    }
}*/

/*abstract class AbstractReceivedItemAdapter<T>(
    protected val itemRealmResults: RealmResults<T>,
    protected val listener: OnPushItemListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: McvToolFromDbBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.mcv_tool_from_db, parent, false)

        return getViewHolder(binding)
    }

    abstract fun getViewHolder(binding: McvToolFromDbBinding):RecyclerView.ViewHolder

    override fun getItemCount(): Int = itemRealmResults.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.bind(itemRealmResults[position])
        holder.binding.imageButtonToolToPass.setOnClickListener {
            val id = itemRealmResults[position]?.id
            listener.onPushItemClick(id)
        }
    }
}*/