package com.peacecodes.timetablemanager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.databinding.FragmentSignInBinding
import com.peacecodes.timetablemanager.databinding.FragmentSignUpBinding

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.loginButton.setOnClickListener{
            navController.navigate(R.id.action_signInFragment_to_home)
        }
    }
}