package pl.podwikagrzegorz.gardener.ui.price_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.NoteRealm
import pl.podwikagrzegorz.gardener.databinding.ListMaterialcardviewBinding

class NoteAdapter(
    private val noteRealmResults: RealmResults<NoteRealm>,
    private val listener: OnDeleteItemListener
) : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val inflater = LayoutInflater.from(parent.context)

        val materialCVBinding =
            DataBindingUtil.inflate<ListMaterialcardviewBinding>(inflater, R.layout.list_materialcardview, parent, false)

        return NoteHolder(materialCVBinding)
    }

    override fun getItemCount(): Int = noteRealmResults.size

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        val realmNote = noteRealmResults[position]

        if (realmNote != null){
            holder.bind(realmNote)
            holder.noteToDeletionIB.setOnClickListener {
                val id = noteRealmResults[position]?.id
                listener.onDeleteItemClick(id)
                //notifyItemRemoved(position)
            }
        }
    }


    class NoteHolder(private val binding: ListMaterialcardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val noteToDeletionIB = binding.imageButtonServiceToDelete

        fun bind(note: NoteRealm){
            binding.textViewService.text = note.service
            binding.textViewPriceOfService.text = note.priceOfService
        }

    }

}