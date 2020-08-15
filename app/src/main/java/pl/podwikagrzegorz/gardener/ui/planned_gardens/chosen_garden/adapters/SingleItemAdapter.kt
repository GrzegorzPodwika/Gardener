package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.ActiveString
import pl.podwikagrzegorz.gardener.databinding.McvSingleItemBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class SingleItemAdapter(
    private val listener: OnClickItemListener
) : ListAdapter<ActiveString, SingleItemAdapter.SingleItemHolder>(ItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemHolder {
        return SingleItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SingleItemHolder, position: Int) {
        val activeString = getItem(position)
        holder.bind(activeString, listener, position)
    }

    class SingleItemHolder(val binding: McvSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activeString: ActiveString, listener: OnClickItemListener, position: Int) {
            binding.activeString = activeString
            setUpBindingListeners(listener, position)
            binding.executePendingBindings()
        }

        private fun setUpBindingListeners(listener: OnClickItemListener, position: Int) {
            binding.textViewItemName.setOnClickListener {
                listener.onChangeFlagToOpposite(position)
            }

            binding.imageButtonItemToDelete.setOnClickListener {
                listener.onClick(position.toLong())
            }
        }

        companion object {
            fun from(parent: ViewGroup): SingleItemHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvSingleItemBinding.inflate(layoutInflater, parent, false)
                return SingleItemHolder(binding)
            }
        }
    }

    object ItemDiffCallback : DiffUtil.ItemCallback<ActiveString>() {
        override fun areItemsTheSame(oldItem: ActiveString, newItem: ActiveString): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ActiveString, newItem: ActiveString): Boolean {
            return oldItem == newItem
        }

    }
}
