package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.databinding.BottomSheetWorkerCheckboxBinding

class SheetAssignWorkerAdapter(
    private val workersList: List<Worker>
): RecyclerView.Adapter<SheetAssignWorkerAdapter.WorkerHolder>() {

    private val listOfCheckedWorkers = MutableList<Boolean>(workersList.size) {false}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        return WorkerHolder.from(parent)
    }

    override fun getItemCount(): Int = workersList.size

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        val worker = workersList[position]
        holder.bind(worker)
        holder.binding.materialCheckBoxReceivedWorkers.setOnCheckedChangeListener { _, isChecked ->
            listOfCheckedWorkers[position] = isChecked
        }
    }

    fun getListOfPickedWorkers(): List<Worker> {
        val pickedWorkers = mutableListOf<Worker>()

        for (i in listOfCheckedWorkers.indices) {
            if (listOfCheckedWorkers[i]) {
                pickedWorkers.add(workersList[i])
            }
        }

        return pickedWorkers
    }

    class WorkerHolder private constructor(val binding: BottomSheetWorkerCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(worker: Worker) {
            binding.worker = worker
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): WorkerHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    BottomSheetWorkerCheckboxBinding.inflate(layoutInflater, parent, false)

                return WorkerHolder(binding)
            }
        }
    }

/*

    class WorkerDiffCallback : DiffUtil.ItemCallback<Worker>() {
        override fun areItemsTheSame(oldItem: Worker, newItem: Worker): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Worker, newItem: Worker): Boolean {
            return oldItem == newItem
        }
    }

*/


}