package pl.podwikagrzegorz.gardener.ui.planned_gardens.basic_garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import pl.podwikagrzegorz.gardener.data.domain.BasicGarden

import pl.podwikagrzegorz.gardener.databinding.McvSingleGardenBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class BasicGardenAdapter(
    options: FirestoreRecyclerOptions<BasicGarden>,
    private val listener: OnClickItemListener
) : FirestoreRecyclerAdapter<BasicGarden, BasicGardenAdapter.BasicGardenHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasicGardenHolder {
        return BasicGardenHolder.from(parent)
    }



    override fun onBindViewHolder(holder: BasicGardenHolder, position: Int, model: BasicGarden) {
        holder.bind(model, listener)
    }

    class BasicGardenHolder private constructor(private val binding: McvSingleGardenBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(basicGarden: BasicGarden, listener: OnClickItemListener) {
            binding.basicGarden = basicGarden
            binding.onClickListener = listener
            binding.root.setOnLongClickListener {
                listener.onLongClick(basicGarden.documentId)
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
}


