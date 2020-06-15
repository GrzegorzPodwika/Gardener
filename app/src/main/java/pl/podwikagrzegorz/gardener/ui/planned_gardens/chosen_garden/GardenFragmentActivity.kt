package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.viewpager_garden.*
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.ui.planned_gardens.PlannedGardensFragment

class GardenFragmentActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var titlesArray : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager_garden)

        titlesArray = resources.getStringArray(R.array.component_garden_names)
        val gardenId = intent.getLongExtra(PlannedGardensFragment.GARDEN_ID, 0)

        viewPager = findViewById(R.id.pager_single_garden)
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = NUMBER_OF_FRAGMENTS

            override fun createFragment(position: Int): Fragment =
                GardenFragmentHolder.create(position, gardenId)
        }

        TabLayoutMediator(tabLayout_component_garden, viewPager ){ tab, position ->
            tab.text = titlesArray[position].substringBefore(' ')
        }.attach()

    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    companion object {
        private const val NUMBER_OF_FRAGMENTS = 9
    }

}
