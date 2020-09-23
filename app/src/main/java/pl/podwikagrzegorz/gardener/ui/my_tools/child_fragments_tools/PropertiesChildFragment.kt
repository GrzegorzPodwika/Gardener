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
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.databinding.FragmentChildPropertiesBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

@AndroidEntryPoint
class PropertiesChildFragment : Fragment() {

    private lateinit var binding: FragmentChildPropertiesBinding
    private val viewModel: PropertiesChildViewModel by viewModels()
    private lateinit var propertyAdapter: PropertyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChildPropertiesBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpBindingWithViewModel()
        observeAddPropertyButton()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Property>()
            .setQuery(viewModel.getQuerySortedByTimestamp(), Property::class.java)
            .setLifecycleOwner(this)
            .build()

        propertyAdapter = PropertyAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModel.deleteProperty(documentId)
            }
        }, object : OnEditItemListener<Property> {
            override fun onEditItem(itemToEdit: Property) {
                showEditItemDialog(itemToEdit)
            }

        })
    }

    private fun showEditItemDialog(propertyToEdit: Property) {
        EditPropertyDialog(propertyToEdit, object : EditPropertyDialog.OnChangedPropertyListener {
            override fun onChangedProperty(newProperty: Property) {
                viewModel.updateProperty(newProperty)
            }
        }).show(childFragmentManager, null)
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            propertiesChildViewModel = viewModel
            recyclerViewMyProperties.adapter = propertyAdapter
            propertyName = ""
            numbOfPropertiesAsString = ""
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
        toast(getString(R.string.fill_up_field))
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

}
