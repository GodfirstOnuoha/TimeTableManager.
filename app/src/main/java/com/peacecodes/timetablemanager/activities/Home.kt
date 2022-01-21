package com.peacecodes.timetablemanager.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.peacecodes.timetablemanager.adapters.ViewPagerAdapter
import com.peacecodes.timetablemanager.databinding.ActivityHomeBinding

class Home : AppCompatActivity() {
private lateinit var binding: ActivityHomeBinding
    var tabTitle =
        arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {

        }

        binding.apply {
            // connecting the viewpager and the adapter class
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            //initializing and setting the title of the fragments
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        }

    }

}

