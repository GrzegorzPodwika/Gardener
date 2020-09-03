package pl.podwikagrzegorz.gardener.ui.my_tools

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import pl.podwikagrzegorz.gardener.GardenerApp
import pl.podwikagrzegorz.gardener.R
import pl.podwikagrzegorz.gardener.databinding.FragmentMyToolsBinding
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.MachinesChildFragment
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.PropertiesChildFragment
import pl.podwikagrzegorz.gardener.ui.my_tools.child_fragments_tools.ToolsChildFragment

class MyToolsFragment : Fragment() {

    private lateinit var binding: FragmentMyToolsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMyToolsBinding.inflate(inflater, container, false)

        binding.bottomNavigationTools.setOnNavigationItemSelectedListener(navListener)

        childFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fragment_zoom_in, android.R.anim.fade_out)
            .replace(R.id.tools_fragment_container, ToolsChildFragment())
            .commit()

        return binding.root
    }

    private val navListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_menu_tools -> selectedFragment = ToolsChildFragment()
                R.id.nav_menu_equipment -> selectedFragment = MachinesChildFragment()
                R.id.nav_menu_canister -> selectedFragment = PropertiesChildFragment()
            }

            if (selectedFragment != null) {

                childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.fragment_zoom_in, android.R.anim.fade_out)
                    .replace(R.id.tools_fragment_container, selectedFragment)
                    .commit()
                true
            } else false
        }
}
