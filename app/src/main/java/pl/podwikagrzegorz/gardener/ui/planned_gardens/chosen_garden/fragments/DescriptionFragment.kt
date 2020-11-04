package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

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
import pl.podwikagrzegorz.gardener.data.domain.ActiveString
import pl.podwikagrzegorz.gardener.databinding.FragmentDescriptionBinding
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.OnEditItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.EditActiveStringDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.DescriptionViewModel

@AndroidEntryPoint
class DescriptionFragment : Fragment() {

    private lateinit var binding: FragmentDescriptionBinding
    private val viewModel: DescriptionViewModel by viewModels()
    private lateinit var descriptionAdapter: SingleItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDescriptionBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpViewModelWithBinding()
        observeViewModelData()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<ActiveString>()
            .setQuery(viewModel.getDescriptionQuerySortedByActivity(), ActiveString::class.java)
            .setLifecycleOwner(this)
            .build()

        descriptionAdapter = SingleItemAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModel.deleteDescriptionFromList(childDocumentId = documentId)
            }

            override fun onChangeFlagToOpposite(documentId: String, isActive: Boolean) {
                viewModel.reverseFlagOnDescription(childDocumentId = documentId, isActive)
            }
        }, object : OnEditItemListener<ActiveString> {
            override fun onEditItem(itemToEdit: ActiveString) {
                showEditActiveStringDialog(itemToEdit)
            }
        })
    }

    private fun showEditActiveStringDialog(activeStringToEdit: ActiveString) {
        EditActiveStringDialog(activeStringToEdit, object : EditActiveStringDialog.OnChangedActiveStringListener {
            override fun onChangedActiveString(newActiveString: ActiveString) {
                viewModel.updateDescription(newActiveString)
            }
        }).show(childFragmentManager, null)
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            descriptionViewModel = viewModel
            recyclerViewDescriptionList.adapter = descriptionAdapter
            userDescription = ""
        }
    }

    private fun observeViewModelData() {
        observeDescriptionAdding()
        observeErrorEmptyInput()
    }

    private fun observeDescriptionAdding() {
        viewModel.eventDescriptionAdded.observe(viewLifecycleOwner, Observer { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })
    }

    private fun cleanUp() {
        clearView()
        setFocusToEditTextView()
        viewModel.onAddDescriptionComplete()
    }

    private fun clearView() {
        binding.editTextDescriptionName.text.clear()
    }

    private fun setFocusToEditTextView() {
        binding.editTextDescriptionName.requestFocus()
    }

    private fun observeErrorEmptyInput() {
        viewModel.errorEmptyInput.observe(viewLifecycleOwner, Observer { hasOccurred ->
            if (hasOccurred) {
                toast(getString(R.string.fill_up_field))
                viewModel.onShowErrorComplete()
            }
        })
    }

    companion object {
        fun create(gardenTitle: String): DescriptionFragment {
            val fragment = DescriptionFragment()
            fragment.arguments = toBundle(gardenTitle)
            return fragment
        }
    }
}