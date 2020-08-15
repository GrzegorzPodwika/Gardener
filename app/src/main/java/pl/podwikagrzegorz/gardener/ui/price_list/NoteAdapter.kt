package pl.podwikagrzegorz.gardener.ui.price_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.Note
import pl.podwikagrzegorz.gardener.databinding.McvNotePriceListBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class NoteAdapter(private val listener: OnClickItemListener) :
    ListAdapter<Note, NoteAdapter.NoteHolder>(NoteDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note, listener)
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

    object NoteDiffCallback : DiffUtil.ItemCallback<Note>() {

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}

