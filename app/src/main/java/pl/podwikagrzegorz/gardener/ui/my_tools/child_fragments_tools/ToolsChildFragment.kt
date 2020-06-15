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

    private val toolsVM: ToolsChildViewModel by lazy {
        ViewModelProvider(this).get(ToolsChildViewModel::class.java)
    }
    private lateinit var binding: RecViewAndMcvBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.rec_view_and_mcv, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setOnAddToolListener()
        observeToolData()
    }

    private fun setOnAddToolListener() {
        binding.imageButtonAddTool.setOnClickListener { insertNewUserTool() }
    }

    private fun observeToolData() {
        val recViewTools = binding.recyclerViewMyTools
        recViewTools.layoutManager = LinearLayoutManager(requireContext())

        toolsVM.getToolData().observe(viewLifecycleOwner,
            Observer { tools ->
                recViewTools.also {
                    it.adapter = ToolAdapter(tools, this)
                }
            })
    }

    private fun insertNewUserTool() {
        addTool()
        clearViews()
        setFocusOnFirstEditText()
    }

    private fun addTool() {
        val tool = Tool(
            0,
            binding.editTextMyToolsNameAdd.text.toString(),
            binding.editTextPriceOfTool.text.toString().toInt()
        )
        toolsVM.addTool(tool)
    }

    private fun clearViews() {
        binding.editTextMyToolsNameAdd.text = null
        binding.editTextPriceOfTool.text = null
    }

    private fun setFocusOnFirstEditText() {
        binding.editTextMyToolsNameAdd.requestFocus()
    }

    override fun onDeleteItemClick(id: Long?) {
        toolsVM.deleteTool(id)
    }

    companion object {
        fun newInstance() = ToolsChildFragment()
    }

}
