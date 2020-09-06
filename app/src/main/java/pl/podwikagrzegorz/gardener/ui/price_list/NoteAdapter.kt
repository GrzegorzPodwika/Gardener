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
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import timber.log.Timber

class NoteAdapter(
    options: FirestoreRecyclerOptions<Note>,
    private val listener: OnClickItemListener
) : FirestoreRecyclerAdapter<Note, NoteAdapter.NoteHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, listener)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int, model: Note) {
        holder.bind(model, listener)
    }

    class NoteHolder(private val binding: McvNotePriceListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note, listener: OnClickItemListener) {
            binding.note = note
            binding.clickListener = listener
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

