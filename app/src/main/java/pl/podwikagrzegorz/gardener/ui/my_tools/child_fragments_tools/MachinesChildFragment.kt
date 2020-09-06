package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Machine
import pl.podwikagrzegorz.gardener.databinding.FragmentChildMachinesBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

@AndroidEntryPoint
class MachinesChildFragment : Fragment() {

    private lateinit var binding: FragmentChildMachinesBinding
    private val viewModel: MachinesChildViewModel by viewModels()
    private lateinit var machineAdapter: MachineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildMachinesBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpBindingWithViewModel()
        observeAddMachineButton()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Machine>()
            .setQuery(viewModel.getQuerySortedByTimestamp(), Machine::class.java)
            .setLifecycleOwner(this)
            .build()

        machineAdapter = MachineAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModel.deleteMachine(documentId)
            }
        })
    }

    //private fun getOptionsBaseOnMenuOption(option: MenuOption)

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            machinesChildViewModel = viewModel
            recyclerViewMyMachines.adapter = machineAdapter
            machineName = ""
            numbOfMachinesAsString = ""
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
        toast(getString(R.string.fill_up_field))
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

}
