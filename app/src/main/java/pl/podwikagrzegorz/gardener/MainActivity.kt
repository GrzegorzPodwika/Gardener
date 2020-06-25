package pl.podwikagrzegorz.gardener

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) } //1
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_planned_gardens, R.id.nav_my_tools, R.id.nav_calendar,
                R.id.nav_garden_price_list, R.id.nav_completed_gardens, R.id.nav_workers,
                R.id.nav_settings, R.id.nav_info
            ), drawerLayout
        )
    }
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        setupViews()
    }

    private fun setupNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        setupWithNavController(toolbar, navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.nav_add_garden) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViews() {
        navView.setNavigationItemSelectedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_planned_gardens -> {
                navController.navigate(R.id.nav_planned_gardens)
            }
            R.id.nav_my_tools -> {
                navController.navigate(R.id.nav_my_tools)
            }
            R.id.nav_calendar -> {
                navController.navigate(R.id.nav_calendar)
            }
            R.id.nav_workers -> {
                navController.navigate(R.id.nav_workers)
            }
            R.id.nav_garden_price_list -> {
                navController.navigate(R.id.nav_garden_price_list)
            }
            R.id.nav_completed_gardens -> {
                navController.navigate(R.id.nav_completed_gardens)
            }
            R.id.nav_settings -> {
                navController.navigate(R.id.nav_settings)
            }
            R.id.nav_info -> {
                navController.navigate(R.id.nav_info)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


}
