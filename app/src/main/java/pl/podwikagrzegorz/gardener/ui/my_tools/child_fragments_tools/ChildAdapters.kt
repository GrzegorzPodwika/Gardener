package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.data.realm.ToolRealm
import pl.podwikagrzegorz.gardener.databinding.ListMaterialcardviewBinding
import pl.podwikagrzegorz.gardener.databinding.McvSingleToolBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.MyToolsAbstractAdapter
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class ToolAdapter(
    override val itemRealmResults: RealmResults<ToolRealm>,
    override val listener: OnDeleteItemListener
) : MyToolsAbstractAdapter<ToolRealm>(itemRealmResults, listener) {

    override fun getLayoutId(position: Int, obj: ToolRealm): Int {
        return R.layout.mcv_single_tool
    }

    override fun getViewHolder(
        binding: McvSingleToolBinding,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ToolHolder(binding)
    }


    class ToolHolder(private val binding: McvSingleToolBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<ToolRealm> {

        override fun bind(data: ToolRealm, listener: OnDeleteItemListener) {
            binding.textViewToolName.text = data.toolName
            binding.textViewNumbOfTools.text = data.numberOfTools.toString()
            binding.imageButtonToolToDelete.setOnClickListener {
                listener.onDeleteItemClick(data.id)
            }
        }
    }
}

class MachineAdapter(
    override val itemRealmResults: RealmResults<MachineRealm>,
    override val listener: OnDeleteItemListener
) : MyToolsAbstractAdapter<MachineRealm>(itemRealmResults, listener) {

    override fun getLayoutId(position: Int, obj: MachineRealm): Int {
        return R.layout.mcv_single_tool
    }


    override fun getViewHolder(
        binding: McvSingleToolBinding,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MachineHolder(binding)
    }

    class MachineHolder(private val binding: McvSingleToolBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<MachineRealm> {

        override fun bind(data: MachineRealm, listener: OnDeleteItemListener) {
            binding.textViewToolName.text = data.machineName
            binding.textViewNumbOfTools.text = data.numberOfMachines.toString()
            binding.imageButtonToolToDelete.setOnClickListener {
                listener.onDeleteItemClick(data.id)
            }
        }
    }
}

class PropertyAdapter(
    override val itemRealmResults: RealmResults<PropertyRealm>,
    override val listener: OnDeleteItemListener
) : MyToolsAbstractAdapter<PropertyRealm>(itemRealmResults, listener) {

    override fun getLayoutId(position: Int, obj: PropertyRealm): Int {
        return R.layout.mcv_single_tool
    }

    override fun getViewHolder(
        binding: McvSingleToolBinding,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ToolHolder(binding)
    }


    class ToolHolder(private val binding: McvSingleToolBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<PropertyRealm> {

        override fun bind(data: PropertyRealm, listener: OnDeleteItemListener) {
            binding.textViewToolName.text = data.propertyName
            binding.textViewNumbOfTools.text = data.numberOfProperties.toString()
            binding.imageButtonToolToDelete.setOnClickListener {
                listener.onDeleteItemClick(data.id)
            }
        }
    }
}