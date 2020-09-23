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
import pl.podwikagrzegorz.gardener.databinding.FragmentShoppingBinding
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.OnEditItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.ShoppingViewModel

//Class No7 - Shopping
@AndroidEntryPoint
class ShoppingFragment : Fragment() {

    private lateinit var binding: FragmentShoppingBinding
    private val viewModel: ShoppingViewModel by viewModels()
    private lateinit var shoppingAdapter: SingleItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpViewModelWithBinding()
        observeViewModelData()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<ActiveString>()
            .setQuery(viewModel.getShoppingNotesQuerySortedByActivity(), ActiveString::class.java)
            .setLifecycleOwner(this)
            .build()

        shoppingAdapter = SingleItemAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModel.deleteShoppingNoteFromList(childDocumentId = documentId)
            }

            override fun onChangeFlagToOpposite(documentId: String, isActive: Boolean) {
                viewModel.reverseFlagOnShoppingNote(childDocumentId = documentId, isActive)
            }
        }, object : OnEditItemListener<ActiveString> {
            override fun onEditItem(itemToEdit: ActiveString) {
                showEditActiveStringDialog(itemToEdit)
            }
        })
    }

    private fun showEditActiveStringDialog(itemToEdit: ActiveString) {
        EditActiveStringDialog(itemToEdit, object : EditActiveStringDialog.OnChangedActiveStringListener {
            override fun onChangedActiveString(newActiveString: ActiveString) {
                viewModel.updateShoppingNote(newActiveString)
            }
        }).show(childFragmentManager, null)
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            shoppingViewModel = viewModel
            recyclerViewShoppingList.adapter = shoppingAdapter
            userShoppingNote = ""
        }
    }
    private fun observeViewModelData() {
        observeShoppingNoteAdding()
        observeErrorEmptyInput()
    }

    private fun observeShoppingNoteAdding() {
        viewModel.eventShoppingNoteAdded.observe(viewLifecycleOwner, Observer { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })
    }

    private fun cleanUp() {
        clearView()
        setFocusToEditTextView()
        viewModel.onAddShoppingNoteComplete()
    }

    private fun clearView() {
        binding.editTextShoppingNote.text.clear()
    }

    private fun setFocusToEditTextView() {
        binding.editTextShoppingNote.requestFocus()
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
        fun create(gardenTitle: String): ShoppingFragment {
            val fragment = ShoppingFragment()
            fragment.arguments = toBundle(gardenTitle)
            return fragment
        }
    }
}