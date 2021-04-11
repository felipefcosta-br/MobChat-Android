package br.felipefcosta.mobchat.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.mainToolbar)

        val navHost: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainNavHostFragment) as NavHostFragment

        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.chats_fragment,
            R.id.contacts_fragment,
            R.id.search_fragment
        ).build()

        /*val drawerLayout : DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.all_chats_fragment, R.id.contacts_fragment),
            drawerLayout)*/

        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val returnValeu = super.onCreateOptionsMenu(menu)
        //val navigationView = findViewById<NavigationView>(R.id.)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return item.onNavDestinationSelected(findNavController(R.id.mainNavHostFragment))
                || super.onOptionsItemSelected(item)
    }
    private fun setupActionBar(navController: NavController,
                               appBarConfig : AppBarConfiguration
    ) {

       setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupBottomNavMenu(navController: NavController){
        val mainNav = findViewById<BottomNavigationView>(R.id.main_nav_view)
        mainNav.setupWithNavController(navController)
    }
}