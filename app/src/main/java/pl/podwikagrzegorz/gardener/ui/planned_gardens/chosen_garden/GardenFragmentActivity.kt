package pl.podwikagrzegorz.gardener.ui.planned_gardens.chosen_garden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import pl.podwikagrzegorz.gardener.R

class GardenFragmentActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.viewpager_garden)

        viewPager = findViewById(R.id.pager_single_garden)
        viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = Component.values().size

            override fun createFragment(position: Int): Fragment
                 = ComponentFragmentHolder.create(position)
        }
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed()
        } else {
            // Otherwise, select the previous step.
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }
}
