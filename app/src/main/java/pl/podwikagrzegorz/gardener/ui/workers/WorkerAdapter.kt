package pl.podwikagrzegorz.gardener.ui.workers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.databinding.McvSingleWorkerBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class WorkerAdapter(
    options: FirestoreRecyclerOptions<Worker>,
    private val listener: OnClickItemListener
) : FirestoreRecyclerAdapter<Worker, WorkerAdapter.WorkerHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        return WorkerHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WorkerHolder, position: Int, model: Worker) {
        holder.bind(model, listener)
    }

    class WorkerHolder private constructor(private val binding: McvSingleWorkerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(worker: Worker, listener: OnClickItemListener) {
            binding.worker = worker
            binding.onClickListener = listener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WorkerHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = McvSingleWorkerBinding
                    .inflate(layoutInflater, parent, false)
                return WorkerHolder(binding)
            }
        }
    }

}

