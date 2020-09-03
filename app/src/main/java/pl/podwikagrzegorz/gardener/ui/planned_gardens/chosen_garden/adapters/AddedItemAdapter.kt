package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import pl.podwikagrzegorz.gardener.data.domain.Item
import pl.podwikagrzegorz.gardener.databinding.McvAddedToolBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

//1 adapter for added data : Tool, Machine and Property values separately
class AddedItemAdapter(
    options: FirestoreRecyclerOptions<Item>,
    private val listener: OnClickItemListener
) : FirestoreRecyclerAdapter<Item, AddedItemAdapter.AddedItemHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddedItemHolder {
        return AddedItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AddedItemHolder, position: Int, model: Item) {
        holder.bind(model, listener)
    }

    class AddedItemHolder private constructor(private val binding: McvAddedToolBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item, listener: OnClickItemListener) {
            binding.item = item
            setListenersToViews(item, listener)
            binding.executePendingBindings()
        }

        private fun setListenersToViews(item: Item, listener: OnClickItemListener) {
            binding.textViewAddedTool.setOnClickListener {
                listener.onChangeFlagToOpposite(item.documentId)
            }
/*            binding.textViewNumbOfTools.setOnClickListener {
                listener.onChangeNumberOfItems(item.numberOfItems, position, item.itemName)
            }*/
            binding.imageButtonToolToDelete.setOnClickListener {
                listener.onClickItem(item.documentId)
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

}