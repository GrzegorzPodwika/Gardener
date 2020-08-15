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
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.ViewpagerGardenBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden.fragments.*

class GardenViewPagerFragment : Fragment() {
    private lateinit var binding: ViewpagerGardenBinding
    private val titlesArray: Array<String> by lazy {
        GardenerApp.res.getStringArray(R.array.component_garden_names)
    }
    private val args: GardenViewPagerFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.viewpager_garden, container, false)

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
            0 -> BasicGardenFragment.create(args.flowGardenId)
            1 -> DescriptionFragment.create(args.flowGardenId)
            2 -> NoteFragment.create(args.flowGardenId)
            3 -> ToolFragment.create(args.flowGardenId)
            4 -> MachineFragment.create(args.flowGardenId)
            5 -> PropertyFragment.create(args.flowGardenId)
            6 -> ShoppingFragment.create(args.flowGardenId)
            7 -> ManHoursFragment.create(args.flowGardenId)
            8 -> PhotosFragment.create(args.flowGardenId)
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