package com.example.vinilos.ui.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.vinilos.R
import com.example.vinilos.common.Constant
import com.example.vinilos.common.UserType
import com.google.android.ads.mediationtestsuite.viewmodels.HeaderViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userTypeValue = intent.getStringExtra(Constant.USER_TYPE) ?: UserType.VISITOR.type
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        headerViewModel = ViewModelProvider(this).get(HeaderViewModel::class.java)
        binding.headerLayout.header = headerViewModel

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.artists -> {
                    headerViewModel.setTitleAndAddButtonVisibility("Artistas", false)
                    loadFragment(ArtistFragment(), userTypeValue)
                    true
                }

                R.id.albums -> {
                    headerViewModel.setTitleAndAddButtonVisibility(
                        "Álbumes",
                        UserType.COLLECTOR.type == userTypeValue
                    )
                    loadFragment(AlbumFragment(), userTypeValue)
                    true
                }

                R.id.collectors -> {
                    true
                }

                else -> false
            }
        }

        headerViewModel.setTitleAndAddButtonVisibility(
            "Álbumes",
            UserType.COLLECTOR.type == userTypeValue
        )
        loadFragment(AlbumFragment(), userTypeValue)
    }

    private fun loadFragment(fragment: Fragment, userType: String) {
        val bundle = Bundle()
        bundle.putString(Constant.USER_TYPE, userType)
        fragment.arguments = bundle
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}