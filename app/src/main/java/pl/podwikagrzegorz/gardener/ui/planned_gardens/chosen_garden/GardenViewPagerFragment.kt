package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.ViewpagerGardenBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments.*

@AndroidEntryPoint
class GardenViewPagerFragment : Fragment() {
    private lateinit var binding: ViewpagerGardenBinding
    private val titlesArray: Array<String> by lazy {
        GardenerApp.res.getStringArray(R.array.component_garden_names)
    }
    private val args: GardenViewPagerFragmentArgs by navArgs()
    private lateinit var receivedGardenDocumentId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewpagerGardenBinding.inflate(inflater, container, false)
        receivedGardenDocumentId = args.flowGardenDocumentId

        setUpViewPagerAdapter()
        setUpTabLayoutMediator()

        return binding.root
    }

    private fun setUpViewPagerAdapter() {
        binding.viewPagerGardenComponents.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int =
                NUMBER_OF_FRAGMENTS

            override fun createFragment(position: Int): Fragment =
                getComponentFragment(position)
        }
    }

    private fun getComponentFragment(position: Int): Fragment =
        when (position) {
            0 -> BasicGardenFragment.create(receivedGardenDocumentId)
            1 -> DescriptionFragment.create(receivedGardenDocumentId)
            2 -> NoteFragment.create(receivedGardenDocumentId)
            3 -> ToolFragment.create(receivedGardenDocumentId)
            4 -> MachineFragment.create(receivedGardenDocumentId)
            5 -> PropertyFragment.create(receivedGardenDocumentId)
            6 -> ShoppingFragment.create(receivedGardenDocumentId)
            7 -> ManHoursFragment.create(receivedGardenDocumentId)
            8 -> PhotosFragment.create(receivedGardenDocumentId)
            else -> throw Exception("Program didn't find proper Fragment!")
        }


    private fun setUpTabLayoutMediator() {
        TabLayoutMediator(
            binding.tabLayoutComponentGarden,
            binding.viewPagerGardenComponents
        ) { tab, position ->
            tab.text = titlesArray[position].substringBefore(' ')
        }.attach()
    }

    companion object {
        private const val NUMBER_OF_FRAGMENTS = 9
    }
}