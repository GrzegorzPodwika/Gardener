package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentRecViewWithBottomViewBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.NoteViewModel
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

//Class No3 - Notes
class NoteFragment : Fragment(), OnDeleteItemListener {

    private lateinit var binding: FragmentRecViewWithBottomViewBinding
    private val gardenID: Long by lazy {
        NoteViewModel.fromBundle(requireArguments())
    }
    private val viewModelGarden: NoteViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_rec_view_with_bottom_view,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presetRecyclerView()
        observeListOfNotes()
        setOnAddNoteButtonListener()
    }

    private fun presetRecyclerView() {
        binding.recyclerViewSingleItems.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun observeListOfNotes() {
        viewModelGarden.listOfNotes
            .observe(viewLifecycleOwner, Observer {
                binding.recyclerViewSingleItems.adapter =
                    SingleItemAdapter(
                        it
                    ).apply { setListener(this@NoteFragment) }
            })
    }

    private fun setOnAddNoteButtonListener() {
        binding.imageButtonAddItem.setOnClickListener {
            insertUserNote()
        }
    }

    private fun insertUserNote() {
        insertNoteToViewModel()
        clearView()
        setFocusToEditTextView()

    }

    private fun insertNoteToViewModel() {
        val userNote: String = binding.editTextBotItemName.text.toString()
        viewModelGarden.addNoteToList(userNote)
    }

    private fun clearView() {
        binding.editTextBotItemName.text.clear()
    }

    private fun setFocusToEditTextView() {
        binding.editTextBotItemName.requestFocus()
    }

    override fun onDeleteItemClick(id: Long?) {
        viewModelGarden.deleteItemFromList(id)
    }

    override fun onChangeFlagToOpposite(position: Int) {
        viewModelGarden.reverseFlagOnNote(position)
    }

    companion object {
        fun create(gardenID: Long): NoteFragment {
            val fragment = NoteFragment()
            fragment.arguments = NoteViewModel.toBundle(gardenID)
            return fragment
        }

    }
}