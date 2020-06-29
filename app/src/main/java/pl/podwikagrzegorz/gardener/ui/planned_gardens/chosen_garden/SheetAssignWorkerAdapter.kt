package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.WorkerRealm
import pl.podwikagrzegorz.gardener.databinding.BottomSheetWorkerCheckboxBinding

class SheetAssignWorkerAdapter(
    private val workersList: RealmResults<WorkerRealm>
) : RecyclerView.Adapter<SheetAssignWorkerAdapter.WorkerHolder>() {
    private val isCheckedWorkerList = mutableListOf<Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkerHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: BottomSheetWorkerCheckboxBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.bottom_sheet_worker_checkbox,
            parent,
            false
        )

        return WorkerHolder(binding)
    }

    override fun getItemCount(): Int =
        workersList.size


    override fun onBindViewHolder(holder: WorkerHolder, position: Int) {
        holder.bind(workersList[position])
        holder.binding.materialCheckBoxReceivedWorkers.setOnCheckedChangeListener { _, isChecked ->
            isCheckedWorkerList[position] = isChecked
        }
    }

    fun getListOfWorkersFullName() : List<String> {
        val workersFullName = mutableListOf<String>()

        for (i in isCheckedWorkerList.indices){
            if (isCheckedWorkerList[i]){
                if (workersList[i] != null){
                    workersFullName.add(workersList[i]!!.getFullName())
                }
            }
        }

        return workersFullName
    }

    class WorkerHolder(val binding: BottomSheetWorkerCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workerRealm: WorkerRealm?) {
            workerRealm?.let {
                binding.materialCheckBoxReceivedWorkers.text = it.getFullName()
            }
        }
    }

    init {
        for (index in workersList.indices) {
            isCheckedWorkerList.add(false)
        }
    }
}