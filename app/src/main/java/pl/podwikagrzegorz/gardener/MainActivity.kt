package pl.podwikagrzegorz.gardener

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import pl.podwikagrzegorz.gardener.databinding.ActivityMainBinding
import pl.podwikagrzegorz.gardener.di.GlideApp
import pl.podwikagrzegorz.gardener.extensions.startLoginActivity
import pl.podwikagrzegorz.gardener.ui.auth.AuthViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels()

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val appBarConfiguration by lazy {
        AppBarConfiguration(
            setOf(
                R.id.nav_planned_gardens, R.id.nav_my_tools, R.id.nav_calendar,
                R.id.nav_garden_price_list, R.id.nav_completed_gardens, R.id.nav_workers,
                R.id.nav_settings, R.id.nav_info
            ), binding.drawerLayout
        )
    }
/*    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        NavigationUI.setupActionBarWithNavController(this, navController, binding.drawerLayout)
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
        binding.navView.setNavigationItemSelectedListener(this)
    }

    private fun setupUserInfoIntoDrawer() {
        authViewModel.user?.apply {
            val headerLayout = binding.navView.getHeaderView(0)

            val userPhotoIV = headerLayout.findViewById(R.id.imageView_user_photo) as ImageView
            GlideApp.with(userPhotoIV.context)
                .load(photoUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_place_holder)
                .into(userPhotoIV)

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
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
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
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        authViewModel.logout()
        startLoginActivity()
    }
    
}
