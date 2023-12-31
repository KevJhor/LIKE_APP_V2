package com.example.like_app

import android.os.Bundle
import android.view.Menu
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.like_app.Interface.IEstado
import com.example.like_app.databinding.ActivityMainBinding
import com.example.like_app.ui.fragments.EstadoFragment

class MainActivity : AppCompatActivity(), IEstado {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener {

            val navController = findNavController(R.id.nav_host_fragment_content_main) // Asegúrate de que el ID sea el correcto
            navController.navigate(R.id.fgChat)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.atenClientFragment,R.id.homeFragment,R.id.pregFrecFragment,R.id.list_Rest_Fragment,R.id.notificaciones,
                R.id.listaRestaurantes, R.id.listaTiendas, R.id.fragment_Rest, R.id.gestionRestaurante
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onChangeBackground(backgroundResource: Int) {
        val estadosFragment =
            supportFragmentManager.findFragmentByTag("EstadosFragment") as EstadoFragment?
        estadosFragment?.changeBackground(backgroundResource)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}

// 1615465165149686