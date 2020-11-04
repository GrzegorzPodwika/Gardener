package pl.podwikagrzegorz.gardener.ui.price_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.Note
import pl.podwikagrzegorz.gardener.databinding.FragmentPriceListBinding
import pl.podwikagrzegorz.gardener.extensions.toast
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.OnEditItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

@AndroidEntryPoint
class PriceListFragment : Fragment() {

    private lateinit var binding: FragmentPriceListBinding
    private val viewModel: PriceListViewModel by viewModels()

    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPriceListBinding.inflate(inflater, container, false)

        connectRecyclerViewWithQuery()
        setUpBindingWithViewModel()

        observeData()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<Note>()
            .setQuery(viewModel.getQuerySortedByTimestamp(), Note::class.java)
            .setLifecycleOwner(this)
            .build()

        noteAdapter = NoteAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModel.deleteNote(documentId)
            }
        }, object : OnEditItemListener<Note> {
            override fun onEditItem(itemToEdit: Note) {
                showEditNoteDialog(itemToEdit)
            }
        })
    }

    private fun showEditNoteDialog(noteToEdit: Note) {
        EditNoteDialog(noteToEdit) { updatedItem -> viewModel.updateNote(updatedItem) }
            .show(childFragmentManager, null)
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            priceListViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerViewPriceList.adapter = noteAdapter
            service = ""
            priceOfService = ""
        }
    }

    private fun observeData() {
        observeAddNoteButton()
        observeErrorEmptyInput()
    }

    private fun observeAddNoteButton() {
        viewModel.eventAddNote.observe(viewLifecycleOwner, { hasAdded ->
            if (hasAdded) {
                cleanUp()
            }
        })
    }


    private fun cleanUp() {
        clearViews()
        setFocusOnFirstEditText()
        viewModel.onAddNoteComplete()
    }

    private fun clearViews() {
        binding.editTextService.text.clear()
        binding.editTextPriceOfService.text.clear()
    }

    private fun setFocusOnFirstEditText() {
        binding.editTextService.requestFocus()
    }

    private fun observeErrorEmptyInput() {
        viewModel.errorEmptyInput.observe(viewLifecycleOwner, { hasOccurred ->
            if (hasOccurred) {
                showWarningToast()
            }
        })
    }

    private fun showWarningToast() {
        toast(getString(R.string.fill_up_all_field))
        viewModel.onErrorEmptyInputComplete()
    }

    companion object {
        fun newInstance() = PriceListFragment()
    }
}
