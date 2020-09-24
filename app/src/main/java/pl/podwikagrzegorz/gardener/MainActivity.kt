package pl.podwikagrzegorz.gardener

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import coil.api.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import pl.podwikagrzegorz.gardener.extensions.startLoginActivity
import pl.podwikagrzegorz.gardener.ui.auth.AuthViewModel
import pl.podwikagrzegorz.gardener.ui.planned_gardens.PlannedGardensFragmentDirections

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
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
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation()
        setupViews()
        setupUserInfoIntoDrawer()

        (application as GardenerApp).preferenceRepository.nightModeLive.observe(
            this,
            Observer { nightMode ->
                nightMode?.let { delegate.localNightMode = it }
            })
    }

    private fun setupNavigation() {
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        setupWithNavController(toolbar, navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.nav_add_garden
                || destination.id == R.id.nav_map_fragment
                || destination.id == R.id.nav_garden_view_pager_fragment
            ) {
                toolbar.visibility = View.GONE
            } else {
                toolbar.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViews() {
        navView.setNavigationItemSelectedListener(this)
    }

    private fun setupUserInfoIntoDrawer() {
        authViewModel.user?.apply {
            val headerLayout = navView.getHeaderView(0)

            val userPhotoIV = headerLayout.findViewById(R.id.imageView_user_photo) as ImageView
            userPhotoIV.load(photoUrl)

            val userNameTV = headerLayout.findViewById(R.id.textView_user_name) as TextView
            userNameTV.text = displayName

            val userEmailTV = headerLayout.findViewById(R.id.textView_user_email) as TextView
            userEmailTV.text = email
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(android.R.anim.fade_in)
            .setExitAnim(android.R.anim.fade_out)
            .setPopEnterAnim(android.R.anim.fade_in)
            .setPopExitAnim(android.R.anim.fade_out)
            .build()

        when (item.itemId) {
            R.id.nav_planned_gardens -> {
                navController.navigate(R.id.nav_planned_gardens, null, navOptions)
            }
            R.id.nav_my_tools -> {
                navController.navigate(R.id.nav_my_tools, null, navOptions)
            }
            R.id.nav_calendar -> {
                navController.navigate(R.id.nav_calendar, null, navOptions)
            }
            R.id.nav_workers -> {
                navController.navigate(R.id.nav_workers, null, navOptions)
            }
            R.id.nav_garden_price_list -> {
                navController.navigate(R.id.nav_garden_price_list, null, navOptions)
            }
            R.id.nav_completed_gardens -> {
                navController.navigate(R.id.nav_completed_gardens, null, navOptions)
            }
            R.id.nav_settings -> {
                navController.navigate(R.id.nav_settings, null, navOptions)
            }
            R.id.nav_info -> {
                navController.navigate(R.id.nav_info, null, navOptions)
            }
            R.id.nav_logout -> {
                logout()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        authViewModel.logout()
        startLoginActivity()
    }


}
