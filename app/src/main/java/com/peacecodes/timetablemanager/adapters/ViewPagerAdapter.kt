package com.peacecodes.timetablemanager.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.peacecodes.timetablemanager.fragments.*

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 7
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return MondayFragment()
            1 -> return TuesdayFragment()
            2 -> return WednesdayFragment()
            3 -> return ThursdayFragment()
            4 -> return FridayFragment()
            5 -> return SaturdayFragment()
            6 -> return SundayFragment()
            else -> return MondayFragment()
        }
    }
}