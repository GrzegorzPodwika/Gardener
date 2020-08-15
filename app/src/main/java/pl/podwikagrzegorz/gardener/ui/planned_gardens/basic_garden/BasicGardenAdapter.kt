package pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden

import pl.podwikagrzegorz.gardener.databinding.McvSingleGardenBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class BasicGardenAdapter(
    private val listener: OnClickItemListener
) : ListAdapter<BasicGarden, BasicGardenAdapter.BasicGardenHolder>(BasicGardenDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicGardenHolder {
        return BasicGardenHolder.from(parent)
    }

    override fun onBindViewHolder(holder: BasicGardenHolder, position: Int) {
        val basicGarden = getItem(position)
        holder.bind(basicGarden, listener)
    }

    class BasicGardenHolder private constructor(private val binding: McvSingleGardenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(basicGarden: BasicGarden, listener: OnClickItemListener) {
            binding.basicGarden = basicGarden
            binding.onClickListener = listener
            binding.root.setOnLongClickListener {
                listener.onLongClick(basicGarden.id)
                true
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): BasicGardenHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = McvSingleGardenBinding.inflate(inflater, parent, false)
                return BasicGardenHolder(binding)
            }
        }
    }

    object BasicGardenDiffCallback : DiffUtil.ItemCallback<BasicGarden>() {
        override fun areItemsTheSame(oldItem: BasicGarden, newItem: BasicGarden): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BasicGarden, newItem: BasicGarden): Boolean {
            return oldItem == newItem
        }
    }
}


