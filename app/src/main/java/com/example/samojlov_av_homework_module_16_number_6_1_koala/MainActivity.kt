package com.example.samojlov_av_homework_module_16_number_6_1_koala

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.samojlov_av_homework_module_16_number_6_1_koala.databinding.ActivityMainBinding
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.samojlov_av_homework_module_16_number_6_1_koala.models.Profile
import com.example.samojlov_av_homework_module_16_number_6_1_koala.ui.notes.NotesViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notesViewModel: NotesViewModel

    var currentProfile: Profile? = null
    var currentCheckLogin = false

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
                R.id.navigation_profile, R.id.navigation_notes, R.id.navigation_weather
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
        init()
    }

    private fun init() {
        notesViewModel =
            ViewModelProvider(this)[NotesViewModel::class.java]
        notesViewModel._currentProfile.observe(this) {
            currentProfile = it
        }
        notesViewModel._loginProfile.observe(this) {
            currentCheckLogin = it
        }
    }

    val permissionOfReadImage = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {

        } else {

        }
    }

    fun setProfile(profile: Profile?) {
        currentProfile = profile
        notesViewModel._currentProfile.value = (profile.also { notesViewModel.currentProfile = it })

    }

    fun setCheckLogin(check: Boolean) {
        currentCheckLogin = check
        notesViewModel._loginProfile.value = (check.also { notesViewModel.loginProfile = it })
    }

    fun getDataProfile(): Profile? {
        return currentProfile
    }

    fun getCheckLogin(): Boolean {
        return currentCheckLogin
    }

}