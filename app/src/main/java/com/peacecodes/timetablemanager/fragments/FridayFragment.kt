package com.peacecodes.timetablemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacecodes.timetablemanager.SQLiteHelper
import com.peacecodes.timetablemanager.adapters.RecyclerAdapter
import com.peacecodes.timetablemanager.databinding.FragmentFridayBinding

class FridayFragment : Fragment() {
    private lateinit var bind: FragmentFridayBinding
    private lateinit var sqliteHelper: SQLiteHelper
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bind = FragmentFridayBinding.inflate(inflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sqliteHelper = SQLiteHelper(requireContext())

        layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerAdapter(sqliteHelper.getDayTimeTable("Friday"))
        bind.recyclerView.layoutManager = layoutManager
        bind.recyclerView.adapter = adapter

    }
}