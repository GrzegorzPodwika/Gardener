package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentChildMachinesBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class MachinesChildFragment : Fragment() {

    private lateinit var binding: FragmentChildMachinesBinding
    private val viewModel: MachinesChildViewModel by lazy {
        ViewModelProvider(this).get(MachinesChildViewModel::class.java)
    }
    private val machineAdapter: MachineAdapter by lazy {
        MachineAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteMachineFromDb(id)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildMachinesBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        observeAddMachineButton()
        observeListOfMachines()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            machinesChildViewModel = viewModel
            recyclerViewMyMachines.adapter = machineAdapter
        }
    }


    private fun observeAddMachineButton() {
        viewModel.eventAddMachine.observe(viewLifecycleOwner, Observer { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })

        viewModel.errorEditTextEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            if (isEmpty){
                showErrorToast()
            }
        })
    }

    private fun showErrorToast() {
        context?.toast(getString(R.string.fill_all_fields))
        viewModel.onErrorShowComplete()
    }

    private fun cleanUp() {
        clearViews()
        setFocusOnFirstEditText()
        viewModel.onAddMachineComplete()
    }

    private fun clearViews() {
        binding.editTextMachineNameAdd.text = null
        binding.editTextPriceOfMachine.text = null
    }

    private fun setFocusOnFirstEditText() {
        binding.editTextMachineNameAdd.requestFocus()
    }

    private fun observeListOfMachines() {
        viewModel.listOfMachines.observe(viewLifecycleOwner, Observer { listOfMachines ->
            machineAdapter.submitList(listOfMachines)
        })
    }

    private fun deleteMachineFromDb(id: Long) {
        viewModel.deleteMachine(id)

    }
}
