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
import pl.podwikagrzegorz.gardener.data.pojo.Tool
import pl.podwikagrzegorz.gardener.databinding.RecViewAndMcvBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class ToolsChildFragment private constructor() : Fragment(), OnDeleteItemListener {

    private lateinit var viewModel: ToolsChildViewModel
    private lateinit var binding: RecViewAndMcvBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.rec_view_and_mcv, container, false)
        binding.imageButtonAddTool.setOnClickListener { insertNewUserTool() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ToolsChildViewModel::class.java)
        val recViewTools = binding.recyclerViewMyTools

        viewModel.getToolData().observe(viewLifecycleOwner,
            Observer { tools ->
                recViewTools.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = ToolAdapter(tools, this)
                }
            })
    }

    private fun insertNewUserTool() {
        val tool = Tool(
            0,
            binding.editTextMyToolsNameAdd.text.toString(),
            binding.editTextPriceOfTool.text.toString().toInt()
        )
        viewModel.addTool(tool)
    }

    override fun onDeleteNoteClick(id: Long?) {
        viewModel.deleteTool(id)
    }

    companion object {
        fun newInstance() = ToolsChildFragment()
    }

}
