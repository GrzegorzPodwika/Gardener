package pl.podwikagrzegorz.gardener.ui.workers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.databinding.McvSingleItemBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class WorkerAdapter(
    private val noteRealmResults: RealmResults<WorkerRealm>,
    private val listener: OnDeleteItemListener
) : RecyclerView.Adapter<WorkerAdapter.WorkerHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<McvSingleItemBinding>(
            layoutInflater, R.layout.mcv_single_item,
            parent, false
        )
        return WorkerHolder(binding)
    }

    override fun getItemCount(): Int =
        noteRealmResults.size

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        holder.bind(noteRealmResults[position])

        holder.binding.imageButtonItemToDelete.setOnClickListener{
            listener.onDeleteItemClick(noteRealmResults[position]?.id)
        }
    }

    class WorkerHolder(val binding: McvSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(worker: WorkerRealm?) {
            worker?.let {
                binding.textViewItemName.text = it.getFullName()
            }
        }
    }
}