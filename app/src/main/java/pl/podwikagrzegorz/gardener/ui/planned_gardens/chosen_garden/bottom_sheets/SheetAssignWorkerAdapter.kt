package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.databinding.BottomSheetWorkerCheckboxBinding

class SheetAssignWorkerAdapter :
    ListAdapter<Worker, SheetAssignWorkerAdapter.WorkerHolder>(WorkerDiffCallback()) {
    private val isCheckedWorkerList = mutableListOf<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        return WorkerHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        val worker = getItem(position)
        holder.bind(worker)
        holder.binding.materialCheckBoxReceivedWorkers.setOnCheckedChangeListener { _, isChecked ->
            isCheckedWorkerList[position] = isChecked
        }
    }

    fun initAndSubmitList(listOfWorkers: List<Worker>) {
        listOfWorkers.forEach { _ -> isCheckedWorkerList.add(false) }
        submitList(listOfWorkers)
    }

    fun getListOfWorkersFullName(): List<String> {
        val workersFullName = mutableListOf<String>()

        for (i in isCheckedWorkerList.indices) {
            if (isCheckedWorkerList[i]) {
                workersFullName.add(getItem(i).fullName)
            }
        }

        return workersFullName
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


    class WorkerDiffCallback : DiffUtil.ItemCallback<Worker>() {
        override fun areItemsTheSame(oldItem: Worker, newItem: Worker): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Worker, newItem: Worker): Boolean {
            return oldItem == newItem
        }
    }

}