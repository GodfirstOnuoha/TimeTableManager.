package com.peacecodes.timetablemanager.activities

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout
import com.peacecodes.timetablemanager.R
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

        binding.apply {
            // connecting the viewpager and the adapter class
            viewPager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

            //initializing and setting the title of the fragments
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()
        }
        addSchedules()

    }
    private fun addSchedules() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.edit_dialog, null)
        builder.setView(view)

        val items = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val adapter = ArrayAdapter(requireContext(), R.layout.edit_dialog, items)
        val selectDay = view.findViewById<TextInputLayout>(R.id.select_day)
        (selectDay.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        val saveBtn = view.findViewById<Button>(R.id.save_btn)

        val alertDialog = builder.create()
        saveBtn.setOnClickListener {
            alertDialog.dismiss()
        }

        binding.floatingActionButton.setOnClickListener {
            alertDialog.show()
        }

    }

}

