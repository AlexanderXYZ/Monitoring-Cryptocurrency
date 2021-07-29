package com.buslaev.monitoringcryptocurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APPLICATION_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.REPOSITORY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APP_ACTIVITY = this
        APPLICATION_ACTIVITY = application
        setupNav()
    }

    private fun setupNav() {
        navController = findNavController(R.id.nav_host_fragment)
        bottom_nav_view.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.allCryptoFragment -> showBottomNav()
                R.id.newsFragment -> showBottomNav()
                R.id.metricsFragment -> {
                    hideBottomNav()
                }
                R.id.profileFragment -> {
                    hideBottomNav()
                }
                R.id.webNewsFragment -> {
                    hideBottomNav()
                }
            }
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()

    }

    private fun showBottomNav() {
        bottom_nav_view.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun hideBottomNav() {
        bottom_nav_view.visibility = View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

}