package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import pl.podwikagrzegorz.gardener.databinding.FragmentNotesBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.NoteViewModel

//Class No3 - Notes
class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding
    private val gardenID: Long by lazy {
        NoteViewModel.fromBundle(requireArguments())
    }
    private val viewModel: NoteViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }

    private val propertyAdapter: SingleItemAdapter by lazy {
        SingleItemAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteNoteFromDb(id)
            }

            override fun onChangeFlagToOpposite(position: Int) {
                reverseFlagOnNote(position)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentNotesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewModelWithBinding()
        observeHasAddedNote()
        observeListOfNotes()
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
        viewModel.eventOnAddedNote.observe(viewLifecycleOwner, Observer { hasAdded ->
            if (hasAdded) {
                cleanUp()
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


    private fun observeListOfNotes() {
        viewModel.listOfNotes.observe(viewLifecycleOwner, Observer { listOfNotes ->
            propertyAdapter.submitList(listOfNotes)
        })
    }


    private fun deleteNoteFromDb(id: Long) {
        viewModel.deleteItemFromList(id)
    }

    private fun reverseFlagOnNote(position: Int) {
        viewModel.reverseFlagOnNote(position)
    }

    companion object {
        fun create(gardenID: Long): NoteFragment {
            val fragment = NoteFragment()
            fragment.arguments = NoteViewModel.toBundle(gardenID)
            return fragment
        }

    }
}