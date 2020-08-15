package pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentChildPropertiesBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class PropertiesChildFragment : Fragment() {

    private lateinit var binding: FragmentChildPropertiesBinding
    private val viewModel: PropertiesChildViewModel by lazy {
        ViewModelProvider(this).get(PropertiesChildViewModel::class.java)
    }
    private val propertyAdapter: PropertyAdapter by lazy {
        PropertyAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deletePropertyFromDb(id)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildPropertiesBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        observeAddPropertyButton()
        observePropertyData()

        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            propertiesChildViewModel = viewModel
            recyclerViewMyProperties.adapter = propertyAdapter
        }
    }

    private fun observeAddPropertyButton() {
        viewModel.eventAddProperty.observe(viewLifecycleOwner, Observer { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })

        viewModel.errorEditTextEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            if (isEmpty) {
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
        viewModel.onAddPropertyComplete()
    }

    private fun clearViews() {
        binding.editTextPropertyName.text = null
        binding.editTextPriceOfProperty.text = null
    }

    private fun setFocusOnFirstEditText() {
        binding.editTextPropertyName.requestFocus()
    }

    private fun observePropertyData() {
        viewModel.listOfProperties.observe(viewLifecycleOwner, Observer { listOfProperties ->
            propertyAdapter.submitList(listOfProperties)
        })
    }


    private fun deletePropertyFromDb(id: Long) {
        viewModel.deleteProperty(id)
    }

}
