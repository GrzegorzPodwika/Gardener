package pl.podwikagrzegorz.gardener.ui.workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Worker
import pl.podwikagrzegorz.gardener.databinding.WorkersFragmentBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

@AndroidEntryPoint
class WorkersFragment : Fragment(), AddWorkerDialog.OnInputListener {

    private lateinit var binding: WorkersFragmentBinding
    private val viewModel: WorkersViewModel by viewModels()
    private lateinit var workerAdapter: WorkerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WorkersFragmentBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpBindingWithViewModel()
        presetRecyclerView()
        setOnAddWorkerFabListener()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Worker>()
            .setQuery(viewModel.getQuerySortedByTimestamp(), Worker::class.java)
            .setLifecycleOwner(this)
            .build()

        workerAdapter = WorkerAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                deleteWorkerFromDb(documentId)
            }
        })
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerViewWorkersList.adapter = workerAdapter
        }
    }

    private fun presetRecyclerView() {
        binding.recyclerViewWorkersList.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.fabAddWorker.visibility == View.VISIBLE)
                    binding.fabAddWorker.hide()
                else if (dy < 0 && binding.fabAddWorker.visibility != View.VISIBLE)
                    binding.fabAddWorker.show()
            }
        })
    }

    private fun setOnAddWorkerFabListener() {
        binding.fabAddWorker.setOnClickListener {
            AddWorkerDialog(this).show(childFragmentManager, "AddWorkerDialog")
        }
    }


    override fun onSendWorkerFullName(workerFullName: String) {
        if (workerFullName.isNotEmpty()) {
            addWorkerIntoViewModel(workerFullName)
        } else {
            toast(getString(R.string.fill_up_field))
        }
    }

    private fun addWorkerIntoViewModel(workerName: String) {
        viewModel.addWorkerIntoDatabase(workerName)
    }

    private fun deleteWorkerFromDb(workerName: String) {
        viewModel.deleteWorker(workerName)
    }

    companion object {
        fun newInstance() = WorkersFragment()
    }
}