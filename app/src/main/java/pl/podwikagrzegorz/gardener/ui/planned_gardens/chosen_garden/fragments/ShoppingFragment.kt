package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import pl.podwikagrzegorz.gardener.databinding.FragmentShoppingBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.SingleItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.ShoppingViewModel

//Class No7 - Shopping
class ShoppingFragment : Fragment() {

    private lateinit var binding: FragmentShoppingBinding
    private val gardenID: Long by lazy {
        ShoppingViewModel.fromBundle(requireArguments())
    }
    private val viewModel: ShoppingViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private val shoppingAdapter: SingleItemAdapter by lazy {
        SingleItemAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteShoppingNoteFromDb(id)
            }

            override fun onChangeFlagToOpposite(position: Int) {
                reverseFlagOnShoppingNote(position)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingBinding.inflate(inflater, container, false)

        setUpViewModelWithBinding()
        observeHasAddedShoppingNote()
        observeListOfShopping()

        return binding.root
    }

    private fun setUpViewModelWithBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            shoppingViewModel = viewModel
            recyclerViewShoppingList.adapter = shoppingAdapter
            userShoppingNote = ""
        }
    }

    private fun observeHasAddedShoppingNote() {
       viewModel.eventOnAddedShoppingNote.observe(viewLifecycleOwner , Observer { hasAdded ->
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

    private fun observeListOfShopping() {
        viewModel.listOfShopping.observe(viewLifecycleOwner, Observer { listOfShopping ->
            shoppingAdapter.submitList(listOfShopping)
        })
    }

    private fun deleteShoppingNoteFromDb(id: Long) {
        viewModel.deleteShoppingNoteFromList(id)
    }

    private fun reverseFlagOnShoppingNote(position: Int) {
        viewModel.reverseFlagOnShoppingNote(position)
    }

    companion object {
        fun create(gardenID: Long): ShoppingFragment {
            val fragment = ShoppingFragment()
            fragment.arguments = ShoppingViewModel.toBundle(gardenID)
            return fragment
        }
    }
}