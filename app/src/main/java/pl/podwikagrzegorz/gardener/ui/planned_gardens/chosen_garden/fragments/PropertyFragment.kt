package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.RealmResults
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.realm.ItemRealm
import pl.podwikagrzegorz.gardener.data.realm.PropertyRealm
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.PropertiesChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.AddedItemAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.PickNumberDialog
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.bottom_sheets.SheetToolsFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.GardenViewModelFactory
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.PropertyViewModel
import pl.podwikagrzegorz.gardener.ui.price_list.OnDeleteItemListener

//Class No6 - Properties
class PropertyFragment : Fragment(), OnDeleteItemListener {

    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val gardenID: Long by lazy {
        PropertyViewModel.fromBundle(requireArguments())
    }
    private val viewModelMainProperties: PropertyViewModel by viewModels {
        GardenViewModelFactory(
            gardenID
        )
    }
    private val viewModelReceivedProperties: PropertiesChildViewModel by lazy {
        ViewModelProvider(this).get(PropertiesChildViewModel::class.java)
    }
    private val receivedProperties: RealmResults<PropertyRealm> by lazy {
        viewModelReceivedProperties.getListOfPropertiesAsRealmResults()
    }

    private val receivedPropertyNames = mutableListOf<String>()
    private lateinit var adapter: AddedItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tools_in_viewpager,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presetRecyclerView()
        presetAddPropertyButton()
        setOnAddPropertiesButtonListener()
        mapReceivedPropertiesIntoNames()
        observeListOfAddedProperties()
    }

    private fun presetRecyclerView() {
        binding.recyclerViewAddedTools.layoutManager =
            LinearLayoutManager(requireContext())
    }

    private fun presetAddPropertyButton() {
        binding.materialButtonAddTools.text = getString(R.string.others)
        binding.materialButtonAddTools.icon = GardenerApp.res.getDrawable(R.drawable.ic_canister, null)
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
                receivedProperties[index]?.let {
                    val propertyToAdd = ItemRealm(it.propertyName, it.numberOfProperties)
                    listOfItemRealm.add(propertyToAdd)
                }
            }
        }

        viewModelMainProperties.addListOfPickedProperties(listOfItemRealm)
    }

    private fun mapReceivedPropertiesIntoNames() {
        receivedProperties.forEach { receivedPropertyNames.add(it.propertyName) }
    }

    private fun observeListOfAddedProperties() {
        viewModelMainProperties.listOfProperties
            .observe(viewLifecycleOwner, Observer {
                adapter = AddedItemAdapter(it, this)
                binding.recyclerViewAddedTools.adapter = adapter
            })
    }


    override fun onChangeNumberOfItems(currentValue: Int, position: Int, itemName: String) {
        val maxValue: Int = viewModelReceivedProperties.findMaxValueOf(itemName)

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

    override fun onDeleteItemClick(id: Long?) {
        viewModelMainProperties.deleteItemFromList(id)
    }

    override fun onChangeFlagToOpposite(position: Int) {
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
