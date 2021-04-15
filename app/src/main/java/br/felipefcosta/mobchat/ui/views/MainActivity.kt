package br.felipefcosta.mobchat.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import br.felipefcosta.mobchat.R
import br.felipefcosta.mobchat.api.AuthApiService
import br.felipefcosta.mobchat.api.ProfileApiService
import br.felipefcosta.mobchat.api.SignalRApiService
import br.felipefcosta.mobchat.databinding.ActivityMainBinding
import br.felipefcosta.mobchat.models.repositories.AuthRepository
import br.felipefcosta.mobchat.models.repositories.ChatHubRepository
import br.felipefcosta.mobchat.models.repositories.ProfileRepository
import br.felipefcosta.mobchat.models.services.*
import br.felipefcosta.mobchat.viewmodels.MainActivityViewModel
import br.felipefcosta.mobchat.viewmodels.MainViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val encryptionManager = EncryptionManager(applicationContext)
        val tokenStorageManager = TokenStorageManager(applicationContext, encryptionManager)
        val profileStorageManager = ProfileStorageManager(applicationContext, encryptionManager)

        val authService = AuthApiService.create()
        val authDataSource = AuthDataSource(authService)
        val authRepository = AuthRepository(authDataSource, tokenStorageManager)

        val signalRApiService = SignalRApiService()
        val chatHubDataSource = ChatHubDataSource(signalRApiService)
        val chatHubRepository = ChatHubRepository(chatHubDataSource)

        val profileService = ProfileApiService.create()
        val profileDataSource = ProfileDataSource(profileService)
        val profileRepository =
            ProfileRepository(profileDataSource, tokenStorageManager, profileStorageManager)

        viewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(application, chatHubRepository, authRepository, profileRepository)
            ).get(
                MainActivityViewModel::class.java
            )
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        lifecycle.addObserver(viewModel)

        val navHost: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainNavHostFragment) as NavHostFragment

        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.main_chats_fragment,
            R.id.contacts_fragment,
            R.id.search_fragment
        ).build()

        /*val drawerLayout : DrawerLayout? = findViewById(R.id.drawer_layout)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.all_chats_fragment, R.id.contacts_fragment),
            drawerLayout)*/
        setSupportActionBar(binding.mainToolbar)
        setupActionBar(navController, appBarConfiguration)
        setupBottomNavMenu(navController)

        val mainNav = findViewById<BottomNavigationView>(R.id.main_nav_view)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.chat_fragment -> mainNav.visibility = View.GONE
                else -> mainNav.visibility = View.VISIBLE

            }
        }
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


    private fun setupBottomNavMenu(navController: NavController) {
        val mainNav = findViewById<BottomNavigationView>(R.id.main_nav_view)
        mainNav.setupWithNavController(navController)
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {

        setupActionBarWithNavController(navController, appBarConfig)
    }

}