package pl.podwikagrzegorz.gardener.ui.price_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import pl.podwikagrzegorz.gardener.databinding.FragmentPriceListBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener

class PriceListFragment : Fragment() {

    private lateinit var binding: FragmentPriceListBinding
    private val viewModel: PriceListViewModel by lazy {
        ViewModelProvider(this).get(PriceListViewModel::class.java)
    }
    private val noteAdapter: NoteAdapter by lazy {
        NoteAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                viewModel.deleteNote(id)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPriceListBinding.inflate(inflater, container, false)

        setUpBindingWithViewModel()
        observeData()
        return binding.root
    }

    private fun setUpBindingWithViewModel() {
        binding.apply {
            priceListViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
            recyclerViewPriceList.adapter = noteAdapter
        }
    }

    private fun observeData() {
        observeAddNoteButton()
        observeListOfNotes()
    }

    private fun observeAddNoteButton() {
        viewModel.eventAddNote.observe(viewLifecycleOwner, Observer { hasAdded ->
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

    private fun observeListOfNotes() {
        viewModel.priceList.observe(viewLifecycleOwner, Observer { listOfNotes ->
            noteAdapter.submitList(listOfNotes)
        })
    }


    companion object {
        fun newInstance() = PriceListFragment()
    }

}
