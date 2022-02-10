package com.peacecodes.timetablemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.databinding.FragmentSignInBinding
import com.peacecodes.timetablemanager.db.DatabaseHelper
import com.peacecodes.timetablemanager.util.Util

class SignInFragment : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var navController: NavController
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initObjects()

        binding.loginButton.setOnClickListener {
           validateUserInputs()
        }
        binding.tvSignUp.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun initObjects() {
        databaseHelper = DatabaseHelper(requireActivity())

    }

    private fun validateUserInputs() {
        Util.hideKeyboard(requireActivity())
        binding.apply {
            when {
                regNo.text!!.isEmpty() -> {
                    Util.displayLongMessage(requireContext(), "Enter your Reg number.")
                    regNo.requestFocus()
                    return
                }
                password.text!!.isEmpty() -> {
                    Util.displayLongMessage(requireContext(), "Enter your password.")
                    password.requestFocus()
                    return
                }
                else -> {
                    verifyFromSQLite(binding)
                    emptyInputEditText()
                }
            }
        }
    }
    private fun verifyFromSQLite(binding: FragmentSignInBinding) {

            if (databaseHelper.checkUser(
                    binding.regNo.text.toString().trim { it <= ' ' },
                    binding.password.text.toString().trim { it <= ' ' })
            ) {
                navController.navigate(R.id.action_signInFragment_to_home)
            } else {
                Util.displayLongMessage(requireContext(), "Login Failed, Register to Login")
                emptyInputEditText()
            }
        }

        private fun emptyInputEditText() {
            binding.regNo.text = null
            binding.password.text = null
        }
    }