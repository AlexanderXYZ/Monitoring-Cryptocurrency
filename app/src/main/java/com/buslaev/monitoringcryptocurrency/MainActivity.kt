package com.buslaev.monitoringcryptocurrency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buslaev.monitoringcryptocurrency.db.CryptoDatabase
import com.buslaev.monitoringcryptocurrency.repository.CryptoRepository
import com.buslaev.monitoringcryptocurrency.utilits.APPLICATION_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.APP_ACTIVITY
import com.buslaev.monitoringcryptocurrency.utilits.REPOSITORY
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        APP_ACTIVITY = this
        APPLICATION_ACTIVITY = application
        navController = findNavController(R.id.nav_host_fragment)

        bottom_nav_view.setupWithNavController(findNavController(R.id.nav_host_fragment))
    }

}