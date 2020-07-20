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
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.DescriptionViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

class DescriptionFragment : Fragment(), OnDeleteItemListener {

    private lateinit var binding: FragmentRecViewWithBottomViewBinding
    private val gardenID: Long by lazy {
        DescriptionViewModel.fromBundle(requireArguments())
    }
    private val viewModelGarden: DescriptionViewModel by viewModels {
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
        observeListOfDescriptions()
        setOnAddDescriptionButtonListener()
    }

    private fun presetRecyclerView() {
        binding.recyclerViewSingleItems.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun observeListOfDescriptions() {
        viewModelGarden.listOfDescriptions
            .observe(viewLifecycleOwner, Observer {
                binding.recyclerViewSingleItems.adapter =
                    SingleItemAdapter(
                        it
                    ).apply { setListener(this@DescriptionFragment) }

            })
    }

    private fun setOnAddDescriptionButtonListener() {
        binding.imageButtonAddItem.setOnClickListener {
            insertUserDescription()
        }
    }

    private fun insertUserDescription() {
        insertDescriptionToViewModel()
        clearView()
        setFocusToEditTextView()
    }

    private fun insertDescriptionToViewModel() {
        val userDescription: String = binding.editTextBotItemName.text.toString()
        viewModelGarden.addDescriptionToList(userDescription)
    }

    private fun clearView() {
        binding.editTextBotItemName.text.clear()
    }

    private fun setFocusToEditTextView() {
        binding.editTextBotItemName.requestFocus()
    }

    override fun onDeleteItemClick(id: Long?) {
        viewModelGarden.deleteDescriptionFromList(id)
    }

    override fun onChangeFlagToOpposite(position: Int) {
        viewModelGarden.reverseFlagOnDescription(position)
    }

    companion object {
        fun create(gardenID: Long): DescriptionFragment {
            val fragment = DescriptionFragment()
            fragment.arguments = DescriptionViewModel.toBundle(gardenID)
            return fragment
        }

    }
}