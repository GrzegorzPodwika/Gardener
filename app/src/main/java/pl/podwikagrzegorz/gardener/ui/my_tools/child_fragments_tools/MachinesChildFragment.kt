package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.pojo.Machine
import pl.podwikagrzegorz.gardener.databinding.RecViewAndMcvBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class MachinesChildFragment private constructor() : Fragment(), OnDeleteItemListener {

    private lateinit var viewModel: MachinesChildViewModel
    private lateinit var binding: RecViewAndMcvBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.rec_view_and_mcv, container, false)
        binding.imageButtonAddTool.setOnClickListener { insertNewUserMachine() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MachinesChildViewModel::class.java)
        val recViewMachines = binding.recyclerViewMyTools

        viewModel.getMachineData().observe(viewLifecycleOwner,
            Observer { machines ->
                recViewMachines.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = MachineAdapter(machines, this)
                }
            })
    }

    private fun insertNewUserMachine() {
        val machine = Machine(
            0,
            binding.editTextMyToolsNameAdd.text.toString(),
            binding.editTextPriceOfTool.text.toString().toInt()
        )
        viewModel.addMachine(machine)
    }

    override fun onDeleteItemClick(id: Long?) {
        viewModel.deleteMachine(id)
    }

    companion object {
        fun newInstance() = MachinesChildFragment()
    }

}
