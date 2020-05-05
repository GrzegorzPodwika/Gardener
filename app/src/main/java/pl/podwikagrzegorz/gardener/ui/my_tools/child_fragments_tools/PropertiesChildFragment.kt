package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import androidx.lifecycle.ViewModelProviders
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
import pl.podwikagrzegorz.gardener.data.pojo.Property
import pl.podwikagrzegorz.gardener.databinding.RecViewAndMcvBinding
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class PropertiesChildFragment private constructor() : Fragment(), OnDeleteItemListener {

    private lateinit var viewModel: PropertiesChildViewModel
    private lateinit var binding: RecViewAndMcvBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.rec_view_and_mcv, container, false)
        binding.imageButtonAddTool.setOnClickListener { insertNewUserProperty() }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PropertiesChildViewModel::class.java)
        val recViewProperties = binding.recyclerViewMyTools

        viewModel.getPropertyData().observe(viewLifecycleOwner,
            Observer { properties ->
                recViewProperties.also {
                    it.layoutManager = LinearLayoutManager(requireContext())
                    it.adapter = PropertyAdapter(properties, this)
                }
            })
    }

    private fun insertNewUserProperty() {
        val property = Property(
            0,
            binding.editTextMyToolsNameAdd.text.toString(),
            binding.editTextPriceOfTool.text.toString().toInt()
        )
        viewModel.addProperty(property)
    }

    companion object {
        fun newInstance() = PropertiesChildFragment()
    }

    override fun onDeleteNoteClick(id: Long?) {
        viewModel.deleteProperty(id)
    }

}
