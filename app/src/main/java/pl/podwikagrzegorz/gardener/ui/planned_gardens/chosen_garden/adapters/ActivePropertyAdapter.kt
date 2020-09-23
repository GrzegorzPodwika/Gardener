package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import pl.podwikagrzegorz.gardener.data.domain.ActiveProperty
import pl.podwikagrzegorz.gardener.databinding.McvAddedPropertyBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class ActivePropertyAdapter(
    options: FirestoreRecyclerOptions<ActiveProperty>,
    private val listener: OnClickItemListener
) : FirestoreRecyclerAdapter<ActiveProperty, ActivePropertyAdapter.ActivePropertyHolder>(options){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivePropertyHolder {
        return ActivePropertyHolder.from(parent)
    }

    override fun onBindViewHolder(
        holder: ActivePropertyHolder,
        position: Int,
        model: ActiveProperty
    ) {
        holder.bind(model, listener)
    }

    class ActivePropertyHolder private constructor(private val binding: McvAddedPropertyBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(activeProperty: ActiveProperty, listener: OnClickItemListener) {
            binding.activeProperty = activeProperty
            setListenersToViews(activeProperty, listener)
            binding.executePendingBindings()
        }

        private fun setListenersToViews(
            activeProperty: ActiveProperty,
            listener: OnClickItemListener
        ) {
            binding.textViewAddedProperty.setOnClickListener {
                listener.onChangeFlagToOpposite(activeProperty.documentId, activeProperty.isActive)
            }

/*            binding.textViewAmountOfProperties.setOnClickListener {
                listener.onChangeNumberOfItems()
            }*/

            binding.imageButtonPropertyToDelete.setOnClickListener {
                listener.onClickItem(activeProperty.documentId)
            }
        }


        companion object {
            fun from(parent: ViewGroup): ActivePropertyHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvAddedPropertyBinding.inflate(layoutInflater, parent, false)

                return ActivePropertyHolder(binding)
            }
        }
    }
}