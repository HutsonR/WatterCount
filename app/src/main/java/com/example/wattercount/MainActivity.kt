package com.example.wattercount

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.wattercount.Fragments.HomeFragment
import com.example.wattercount.Fragments.StatisticFragment
import com.example.wattercount.databinding.ActivityMainBinding

private const val INTERVAL_THREE_HOUR = 10800000L // 10800000L
class MainActivity : AppCompatActivity() {
    private val TAG = "debugTag"

    private lateinit var binding: ActivityMainBinding
    private var alarmManager: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(this, NotificationReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        }
        alarmManager?.setInexactRepeating(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + INTERVAL_THREE_HOUR,
            INTERVAL_THREE_HOUR,
            alarmIntent
        )

        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_stats -> replaceFragment(StatisticFragment())
//                R.id.nav_profile -> replaceFragment(ProfileFragment())
                else -> {}
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

}