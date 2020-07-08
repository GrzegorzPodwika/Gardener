package pl.podwikagrzegorz.gardener.ui.workers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.WorkersFragmentBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class WorkersFragment : Fragment(), OnDeleteItemListener, AddWorkerDialog.OnInputListener{

    private lateinit var workersBinding: WorkersFragmentBinding
    private val viewModel: WorkersViewModel by lazy {
        ViewModelProvider(this).get(WorkersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        workersBinding = DataBindingUtil.inflate(inflater, R.layout.workers_fragment, container, false)
        return workersBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setRecyclerViewWithListOfWorkers()
        setOnAddWorkerFabListener()
    }

    private fun setRecyclerViewWithListOfWorkers() {
        presetRecyclerView()
        observeListOfWorkers()
    }

    private fun presetRecyclerView() {
        workersBinding.recyclerViewWorkersList.layoutManager = LinearLayoutManager(requireContext())
        workersBinding.recyclerViewWorkersList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && workersBinding.fabAddWorker.visibility == View.VISIBLE)
                    workersBinding.fabAddWorker.hide()
                else if (dy < 0 && workersBinding.fabAddWorker.visibility != View.VISIBLE)
                    workersBinding.fabAddWorker.show()
            }
        })
    }

    private fun observeListOfWorkers() {
        viewModel.listOfWorkers.observe(viewLifecycleOwner, Observer { listOfWorkers ->
            workersBinding.recyclerViewWorkersList.adapter = WorkerAdapter(listOfWorkers, this)
        })
    }

    private fun setOnAddWorkerFabListener() {
        workersBinding.fabAddWorker.setOnClickListener {
            AddWorkerDialog(this).show(childFragmentManager, "AddWorkerDialog")
        }
    }

    override fun sendInput(workerFullName: String) {
        val nameList = workerFullName.split(Regex(" "))

        if (nameList.size == 2){
            val name = nameList[0]
            val surname = nameList[1]

            addWorkerIntoViewModel(name, surname)
        }else {
            requireContext().toast(getString(R.string.fill_all_fields))
        }
    }

    private fun addWorkerIntoViewModel(name: String, surname: String) {
        viewModel.addWorkerIntoDatabase(name, surname)
    }

    override fun onDeleteItemClick(id: Long?) {
        id?.let {
            viewModel.deleteWorker(id)
        }
    }
    companion object {
        fun newInstance() = WorkersFragment()

    }
}