package com.example.a912b

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.a912b.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var airplaneModeReceiver: AirplaneModeReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                 R.id.navigation_dashboard, R.id.navigation_notifications,R.id.navigation_home, R.id.navigation_recordaudio, R.id.navigation_recordvideo
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Inicializar el BroadcastReceiver
        airplaneModeReceiver = AirplaneModeReceiver()

        // Registrar el BroadcastReceiver para escuchar cambios en el modo avión
        val intentFilter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        registerReceiver(airplaneModeReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Desregistrar el BroadcastReceiver para evitar fugas de memoria
        unregisterReceiver(airplaneModeReceiver)
    }
}