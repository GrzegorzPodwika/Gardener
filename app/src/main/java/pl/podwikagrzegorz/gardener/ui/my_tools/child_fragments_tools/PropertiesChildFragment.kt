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
import pl.podwikagrzegorz.gardener.data.pojo.Property
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.databinding.RecViewAndMcvBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class PropertiesChildFragment : Fragment(), OnDeleteItemListener {

    private val propertiesVM: PropertiesChildViewModel by lazy {
        ViewModelProvider(this).get(PropertiesChildViewModel::class.java)
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

        setOnAddPropertyListener()
        observePropertyData()
    }

    private fun setOnAddPropertyListener() {
        binding.imageButtonAddTool.setOnClickListener { insertNewUserProperty() }
    }

    private fun observePropertyData() {
        val recViewProperties = binding.recyclerViewMyTools
        recViewProperties.layoutManager = LinearLayoutManager(requireContext())

        propertiesVM.listOfProperties.observe(viewLifecycleOwner,
            Observer { properties ->
                recViewProperties.also {
                    it.adapter = PropertyAdapter(properties, this)
                }
            })
    }

    private fun insertNewUserProperty() {
        addProperty()
        clearViews()
        setFocusOnFirstEditText()
    }

    private fun addProperty() {
        if (areNotEmptyViews()){
            val property = PropertyRealm(
                0,
                binding.editTextMyToolsNameAdd.text.toString(),
                binding.editTextPriceOfTool.text.toString().toInt()
            )
            propertiesVM.addProperty(property)
        } else {
            requireContext().toast(getString(R.string.fill_all_fields))
        }
    }

    private fun areNotEmptyViews(): Boolean {
        return binding.editTextMyToolsNameAdd.text.isNotEmpty() && binding.editTextPriceOfTool.text.isNotEmpty()
    }

    private fun clearViews() {
        binding.editTextMyToolsNameAdd.text = null
        binding.editTextPriceOfTool.text = null
    }

    private fun setFocusOnFirstEditText() {
        binding.editTextMyToolsNameAdd.requestFocus()
    }

    override fun onDeleteItemClick(id: Long?) {
        propertiesVM.deleteProperty(id)
    }

}
