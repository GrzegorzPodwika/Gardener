package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.databinding.McvAddedToolBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

//1 adapter for added data : Tool, Machine and Property values separately
class AddedItemAdapter(
    private val listener: OnClickItemListener
) : ListAdapter<Item, AddedItemAdapter.AddedItemHolder>(AddedItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedItemHolder {
        return AddedItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AddedItemHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener, position)
    }


    class AddedItemHolder(val binding: McvAddedToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, listener: OnClickItemListener, position: Int) {
            binding.item = item
            setListenersToViews(item, listener, position)
            binding.executePendingBindings()
        }

        private fun setListenersToViews(item: Item, listener: OnClickItemListener, position: Int) {
            binding.textViewAddedTool.setOnClickListener {
                listener.onChangeFlagToOpposite(position)
            }
            binding.textViewNumbOfTools.setOnClickListener {
                listener.onChangeNumberOfItems(item.numberOfItems, position, item.itemName)
            }
            binding.imageButtonToolToDelete.setOnClickListener {
                listener.onClick(position.toLong())
            }
        }

        companion object {
            fun from(parent: ViewGroup): AddedItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvAddedToolBinding.inflate(layoutInflater, parent, false)

                return AddedItemHolder(binding)
            }
        }
    }

    object AddedItemDiffCallback : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.itemName == newItem.itemName
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }

    }

}