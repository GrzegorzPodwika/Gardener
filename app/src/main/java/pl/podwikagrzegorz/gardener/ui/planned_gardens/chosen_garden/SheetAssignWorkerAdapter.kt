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
    }

    class WorkerHolder(val binding: BottomSheetWorkerCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(workerRealm: WorkerRealm?){
            workerRealm?.let {
                binding.materialCheckBoxReceivedWorkers.text = it.getFullName()
            }
        }
    }
}