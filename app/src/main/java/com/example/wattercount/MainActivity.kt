package com.example.wattercount

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wattercount.Fragments.HomeFragment
import com.example.wattercount.Fragments.ProfileFragment
import com.example.wattercount.Fragments.StatisticFragment
import com.example.wattercount.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val TAG = "debugTag"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_stats -> replaceFragment(StatisticFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }

//        SharedPreferencesHelper.setOldDateValue(this, "24.06.23")

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}