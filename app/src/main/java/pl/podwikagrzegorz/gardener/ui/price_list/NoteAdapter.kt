package pl.podwikagrzegorz.gardener.ui.price_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import pl.podwikagrzegorz.gardener.data.domain.Note
import pl.podwikagrzegorz.gardener.databinding.McvNotePriceListBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.OnEditItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import timber.log.Timber

class NoteAdapter(
    options: FirestoreRecyclerOptions<Note>,
    private val listener: OnClickItemListener,
    private val editItemListener: OnEditItemListener<Note>
) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int, model: Note) {
        holder.bind(model, listener, editItemListener)
    }

    class NoteHolder(private val binding: McvNotePriceListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            note: Note,
            listener: OnClickItemListener,
            editItemListener: OnEditItemListener<Note>
        ) {
            binding.note = note
            binding.clickListener = listener
            binding.root.setOnLongClickListener {
                editItemListener.onEditItem(note)
                true
            }
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NoteHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvNotePriceListBinding.inflate(layoutInflater, parent, false)
                return NoteHolder(binding)
            }
        }
    }

}

