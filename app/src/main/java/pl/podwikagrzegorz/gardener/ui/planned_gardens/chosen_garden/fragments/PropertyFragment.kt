package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.daos.PropertyDAO
import pl.podwikagrzegorz.gardener.data.domain.Property
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.PropertyViewModel

//Class No6 - Properties
class PropertyFragment : Fragment() {

    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val gardenID: Long by lazy {
        PropertyViewModel.fromBundle(requireArguments())
    }
    private val viewModelMainProperties: PropertyViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }

    private lateinit var receivedProperties: List<Property>
    private val receivedPropertyNames = mutableListOf<String>()

    private val propertyAdapter: AddedItemAdapter by lazy {
        AddedItemAdapter(object : OnClickItemListener {
            override fun onClick(id: Long) {
                deleteMachineFromDb(id)
            }

            override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
                changeNumberOfItems(currentValue, position, itemName)
            }

            override fun onChangeFlagToOpposite(position: Int) {
                changeFlagToOpposite(position)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolsInViewpagerBinding.inflate(inflater, container, false)

        setUpBinding()
        presetAddPropertyButton()
        fetchReceivedProperties()
        mapReceivedPropertiesIntoNames()
        setOnAddPropertiesButtonListener()
        observeListOfAddedProperties()

        return binding.root
    }

    private fun setUpBinding() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            recyclerViewAddedTools.adapter = propertyAdapter
        }
    }

    private fun presetAddPropertyButton() {
        binding.materialButtonAddTools.text = getString(R.string.others)
        binding.materialButtonAddTools.icon =
            GardenerApp.res.getDrawable(R.drawable.ic_canister, null)
    }

    private fun fetchReceivedProperties() {
        val propertyDAO = PropertyDAO()
        receivedProperties = propertyDAO.getDomainData()
        propertyDAO.closeRealm()
    }

    private fun mapReceivedPropertiesIntoNames() {
        receivedProperties.forEach { receivedPropertyNames.add(it.propertyName) }
    }

    private fun setOnAddPropertiesButtonListener() {
        binding.materialButtonAddTools.setOnClickListener {
            SheetToolsFragment(
                receivedPropertyNames,
                object : SheetToolsFragment.OnGetListOfPickedItemsListener {
                    override fun onGetListOfPickedItems(listOfPickedItems: List<Boolean>) {
                        addListOfPickedPropertiesToMainList(listOfPickedItems)
                    }
                }
            ).show(childFragmentManager, null)
        }
    }

    private fun addListOfPickedPropertiesToMainList(listOfPickedItems: List<Boolean>) {
        val listOfItemRealm = mutableListOf<ItemRealm>()

        for (index in listOfPickedItems.indices) {
            if (listOfPickedItems[index]) {
                val propertyToAdd =
                    ItemRealm(
                        receivedProperties[index].propertyName,
                        receivedProperties[index].numberOfProperties
                    )
                listOfItemRealm.add(propertyToAdd)
            }
        }

        viewModelMainProperties.addListOfPickedProperties(listOfItemRealm)
    }

    private fun observeListOfAddedProperties() {
        viewModelMainProperties.listOfProperties.observe(viewLifecycleOwner, Observer { listOfAddedProperties ->
                propertyAdapter.submitList(listOfAddedProperties)
        })
    }

    private fun deleteMachineFromDb(id: Long) {
        viewModelMainProperties.deleteItemFromList(id)
    }

    private fun changeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
        val seekingProperty = receivedProperties.find { it.propertyName == itemName }
        val maxValue: Int = seekingProperty?.numberOfProperties ?: GardenerApp.MAX_NUMBER_OF_MACHINES

        PickNumberDialog(
            currentValue,
            maxValue,
            object :
                PickNumberDialog.OnChosenNumberListener {
                override fun onChosenNumber(chosenNumber: Int) {
                    viewModelMainProperties.updateNumberOfProperties(chosenNumber, position)
                }
            }).show(childFragmentManager, null)
    }

    private fun changeFlagToOpposite(position: Int) {
        viewModelMainProperties.reverseFlagOnProperty(position)
    }

    companion object {
        fun create(gardenID: Long): PropertyFragment {
            val fragment = PropertyFragment()
            fragment.arguments = PropertyViewModel.toBundle(gardenID)
            return fragment
        }
    }
}
