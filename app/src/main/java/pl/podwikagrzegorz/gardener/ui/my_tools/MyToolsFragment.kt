package pl.podwikagrzegorz.gardener.ui.my_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_tools, container, false)
        val bottomNavView = binding.bottomNavigationTools
        bottomNavView.setOnNavigationItemSelectedListener(navListener)
        bottomNavView.itemIconTintList = null

        childFragmentManager.beginTransaction().replace(
            R.id.tools_fragment_container,
            ToolsChildFragment()
        ).commit()

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
                    .replace(R.id.tools_fragment_container, selectedFragment)
                    .commit()
                true
            } else false
        }
}
