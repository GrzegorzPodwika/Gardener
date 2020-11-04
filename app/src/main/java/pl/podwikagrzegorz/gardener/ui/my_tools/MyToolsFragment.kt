package pl.podwikagrzegorz.gardener.ui.my_tools

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        binding = FragmentMyToolsBinding.inflate(inflater, container, false)
        binding.bottomNavigationTools.setOnNavigationItemSelectedListener(navListener)

        replaceChildFragment(ToolsChildFragment())

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
                replaceChildFragment(selectedFragment)
                true
            } else false
        }

    private fun replaceChildFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.tools_fragment_container, fragment)
            .commit()
    }
}
