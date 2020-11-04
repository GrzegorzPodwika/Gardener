package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.data.domain.ActiveProperty
import pl.podwikagrzegorz.gardener.databinding.FragmentToolsInViewpagerBinding
import pl.podwikagrzegorz.gardener.extensions.toBundle
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.PropertiesChildViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.OnClickItemListener
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.adapters.ActivePropertyAdapter
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.dialogs_sheets.SheetPropertiesFragment
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.viewmodels.PropertyViewModel

//Class No6 - Properties
@AndroidEntryPoint
class PropertyFragment : Fragment() {

    private lateinit var binding: FragmentToolsInViewpagerBinding
    private val viewModelMainProperties: PropertyViewModel by viewModels()
    private lateinit var propertyAdapter: ActivePropertyAdapter

    private val receivedPropertiesViewModel: PropertiesChildViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentToolsInViewpagerBinding.inflate(inflater, container, false)
        receivedPropertiesViewModel.preInitialize()

        connectRecyclerViewWithQuery()
        setUpBinding()
        presetAddPropertyButton()
        setOnAddPropertiesButtonListener()

        return binding.root
    }

    private fun connectRecyclerViewWithQuery() {
        val options = FirestoreRecyclerOptions.Builder<ActiveProperty>()
            .setQuery(viewModelMainProperties.getTakenPropertiesQuerySortedByActivity(), ActiveProperty::class.java)
            .setLifecycleOwner(this)
            .build()

        propertyAdapter = ActivePropertyAdapter(options, object : OnClickItemListener {
            override fun onClickItem(documentId: String) {
                viewModelMainProperties.deleteItemFromList(childDocumentId = documentId)
            }

            override fun onChangeFlagToOpposite(documentId: String, isActive: Boolean) {
                viewModelMainProperties.reverseFlagOnProperty(
                    childDocumentId = documentId,
                    isActive
                )
            }
        })
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
            ResourcesCompat.getDrawable(GardenerApp.res, R.drawable.ic_canister, null)
    }


    private fun setOnAddPropertiesButtonListener() {
        binding.materialButtonAddTools.setOnClickListener {
            SheetPropertiesFragment(
                receivedPropertiesViewModel.listOfProperties
            ) { listOfPickedItems ->
                viewModelMainProperties.addListOfPickedProperties(
                    listOfPickedItems
                )
            }.show(childFragmentManager, null)
        }
    }

    companion object {
        fun create(documentId: String): PropertyFragment {
            val fragment = PropertyFragment()
            fragment.arguments = toBundle(documentId)
            return fragment
        }
    }
}