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
import pl.podwikagrzegorz.gardener.databinding.FragmentNotesBinding
import pl.podwikagrzegorz.gardener.extensions.fromBundle
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.OnEditItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.NoteViewModel

//Class No3 - Notes
@AndroidEntryPoint
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var propertyAdapter: SingleItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotesBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpViewModelWithBinding()
        observeHasAddedNote()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<ActiveString>()
            .setQuery(viewModel.getNoteQuerySortedByActivity(), ActiveString::class.java)
            .setLifecycleOwner(this)
            .build()

        propertyAdapter = SingleItemAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModel.deleteItemFromList(childDocumentId = documentId)
            }

            override fun onChangeFlagToOpposite(documentId: String, isActive: Boolean) {
                viewModel.reverseFlagOnNote(childDocumentId = documentId, isActive)
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
                viewModel.updateNote(newActiveString)
            }
        }).show(childFragmentManager, null)
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            noteViewModel = viewModel
            recyclerViewNoteList.adapter = propertyAdapter
            userNote = ""
        }
    }

    private fun observeHasAddedNote() {
        viewModel.eventNoteAdded.observe(viewLifecycleOwner, { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })

        viewModel.errorEmptyInput.observe(viewLifecycleOwner, { hasOccurred ->
            if (hasOccurred) {
                toast(getString(R.string.fill_up_field))
                viewModel.onShowErrorComplete()
            }
        })
    }

    private fun cleanUp() {
        clearView()
        setFocusToEditTextView()
        viewModel.onAddNoteComplete()
    }

    private fun clearView() {
        binding.editTextNote.text.clear()
    }

    private fun setFocusToEditTextView() {
        binding.editTextNote.requestFocus()
    }

    companion object {
        fun create(gardenTitle: String): NoteFragment {
            val fragment = NoteFragment()
            fragment.arguments = toBundle(gardenTitle)
            return fragment
        }

    }
}