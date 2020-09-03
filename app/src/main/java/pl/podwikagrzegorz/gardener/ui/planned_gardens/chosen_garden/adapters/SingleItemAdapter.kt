package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import pl.podwikagrzegorz.gardener.data.domain.ActiveString
import pl.podwikagrzegorz.gardener.databinding.McvSingleItemBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class SingleItemAdapter(
    options: FirestoreRecyclerOptions<ActiveString>,
    private val listener: OnClickItemListener
) : FirestoreRecyclerAdapter<ActiveString, SingleItemAdapter.SingleItemHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleItemHolder {
        return SingleItemHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SingleItemHolder, position: Int, model: ActiveString) {
        holder.bind(model, listener)
    }

    class SingleItemHolder private constructor(private val binding: McvSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activeString: ActiveString, listener: OnClickItemListener) {
            binding.activeString = activeString
            setUpBindingListeners(listener, activeString)
            binding.executePendingBindings()
        }

        private fun setUpBindingListeners(listener: OnClickItemListener, activeString: ActiveString) {
            binding.textViewItemName.setOnClickListener {
                listener.onChangeFlagToOpposite(activeString.documentId)
            }

            binding.imageButtonItemToDelete.setOnClickListener {
                listener.onClickItem(activeString.documentId)
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

}
