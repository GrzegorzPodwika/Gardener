package pl.podwikagrzegorz.gardener.ui.workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.WorkersFragmentBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class WorkersFragment : Fragment(), AddWorkerDialog.OnInputListener {

    private lateinit var binding: WorkersFragmentBinding
    private val viewModel: WorkersViewModel by lazy {
        ViewModelProvider(this).get(WorkersViewModel::class.java)
    }
    private val workerAdapter: WorkerAdapter by lazy {
        WorkerAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteWorkerFromDb(id)
            }
        })
    }

    private fun deleteWorkerFromDb(id: Long) {
        viewModel.deleteWorker(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = WorkersFragmentBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        presetRecyclerView()
        setOnAddWorkerFabListener()
        observeListOfWorkers()

        return binding.root
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

    private fun observeListOfWorkers() {
        viewModel.listOfWorkers.observe(viewLifecycleOwner, Observer { listOfWorkers ->
            workerAdapter.submitList(listOfWorkers)
        })
    }

    override fun onSendWorkerFullName(workerFullName: String) {
        val nameList = workerFullName.split(Regex(" "))

        if (nameList.size == 2) {
            val name = nameList[0]
            val surname = nameList[1]

            addWorkerIntoViewModel(name, surname)
        } else {
            requireContext().toast(getString(R.string.fill_all_fields))
        }
    }

    private fun addWorkerIntoViewModel(name: String, surname: String) {
        viewModel.addWorkerIntoDatabase(name, surname)
    }

    companion object {
        fun newInstance() = WorkersFragment()
    }
}