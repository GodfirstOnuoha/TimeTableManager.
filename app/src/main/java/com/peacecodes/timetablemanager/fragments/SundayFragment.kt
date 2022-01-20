package com.peacecodes.timetablemanager.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.SQLiteHelper
import com.peacecodes.timetablemanager.adapters.RecyclerAdapter
import com.peacecodes.timetablemanager.databinding.FragmentSundayBinding
import com.peacecodes.timetablemanager.models.Data

class SundayFragment : Fragment() {
  private lateinit var bind: FragmentSundayBinding
  private lateinit var sqLiteHelper: SQLiteHelper
  private var layoutManager: RecyclerView.LayoutManager? = null
  private var adapter: RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>? = null

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    bind = FragmentSundayBinding.inflate(inflater, container, false)
    return bind.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    layoutManager = LinearLayoutManager(activity)
    adapter = RecyclerAdapter()
    bind.recyclerView.layoutManager = layoutManager
    bind.recyclerView.adapter = adapter

  }
}