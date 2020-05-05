package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.MachineRealm
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.data.realm.ToolRealm
import pl.podwikagrzegorz.gardener.databinding.ListMaterialcardviewBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.MyToolsAbstractAdapter
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class ToolAdapter(
    override val itemRealmResults: RealmResults<ToolRealm>,
    override val listener: OnDeleteItemListener
) : MyToolsAbstractAdapter<ToolRealm>(itemRealmResults, listener) {

    override fun getLayoutId(position: Int, obj: ToolRealm): Int {
        return R.layout.list_materialcardview
    }

    override fun getViewHolder(
        binding: ListMaterialcardviewBinding,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ToolHolder(binding)
    }


    class ToolHolder(private val binding: ListMaterialcardviewBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<ToolRealm> {
        private val noteToDeletionIB = binding.imageButtonItemToDelete

        override fun bind(data: ToolRealm, listener: OnDeleteItemListener) {
            binding.textViewService.text = data.toolName
            binding.textViewPriceOfService.text = data.numberOfTools.toString()
            noteToDeletionIB.setOnClickListener {
                listener.onDeleteNoteClick(data.id)
            }
        }
    }
}

class MachineAdapter(
    override val itemRealmResults: RealmResults<MachineRealm>,
    override val listener: OnDeleteItemListener
) : MyToolsAbstractAdapter<MachineRealm>(itemRealmResults, listener) {

    override fun getLayoutId(position: Int, obj: MachineRealm): Int {
        return R.layout.list_materialcardview
    }


    override fun getViewHolder(
        binding: ListMaterialcardviewBinding,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return MachineHolder(binding)
    }
    class MachineHolder(private val binding: ListMaterialcardviewBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<MachineRealm> {

        private val noteToDeletionIB = binding.imageButtonItemToDelete
        override fun bind(data: MachineRealm, listener: OnDeleteItemListener) {
            binding.textViewService.text = data.machineName
            binding.textViewPriceOfService.text = data.numberOfMachines.toString()
            noteToDeletionIB.setOnClickListener {
                listener.onDeleteNoteClick(data.id)
            }
        }
    }
}

class PropertyAdapter(
    override val itemRealmResults: RealmResults<PropertyRealm>,
    override val listener: OnDeleteItemListener
) : MyToolsAbstractAdapter<PropertyRealm>(itemRealmResults, listener) {

    override fun getLayoutId(position: Int, obj: PropertyRealm): Int {
        return R.layout.list_materialcardview
    }

    override fun getViewHolder(
        binding: ListMaterialcardviewBinding,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ToolHolder(binding)
    }


    class ToolHolder(private val binding: ListMaterialcardviewBinding) :
        RecyclerView.ViewHolder(binding.root), Binder<PropertyRealm> {
        private val noteToDeletionIB = binding.imageButtonItemToDelete

        override fun bind(data: PropertyRealm, listener: OnDeleteItemListener) {
            binding.textViewService.text = data.propertyName
            binding.textViewPriceOfService.text = data.numberOfProperties.toString()
            noteToDeletionIB.setOnClickListener {
                listener.onDeleteNoteClick(data.id)
            }
        }
    }
}