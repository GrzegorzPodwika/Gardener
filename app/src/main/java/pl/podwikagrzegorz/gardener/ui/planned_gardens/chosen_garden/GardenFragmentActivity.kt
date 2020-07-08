package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.ViewpagerGardenBinding
import pl.podwikagrzegorz.gardener.ui.planned_gardens.PlannedGardensFragment

class GardenFragmentActivity : AppCompatActivity() {

    private lateinit var binding: ViewpagerGardenBinding
    private val titlesArray : Array<String> by lazy {
        GardenerApp.res.getStringArray(R.array.component_garden_names)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager_garden)
        binding = DataBindingUtil.setContentView(this, R.layout.viewpager_garden)

        val gardenId = intent.getLongExtra(PlannedGardensFragment.GARDEN_ID, 0)

        setViewPagerAdapter(gardenId)
        setTabLayoutMediator()
    }

    private fun setViewPagerAdapter(gardenId: Long) {
        binding.viewPagerGardenComponents.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = NUMBER_OF_FRAGMENTS

            override fun createFragment(position: Int): Fragment =
                GardenFragmentHolder.create(position, gardenId)
        }
    }

    private fun setTabLayoutMediator() {
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
