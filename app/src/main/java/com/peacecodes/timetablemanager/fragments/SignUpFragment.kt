package com.peacecodes.timetablemanager.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.peacecodes.timetablemanager.R
import com.peacecodes.timetablemanager.databinding.FragmentSignUpBinding
import com.peacecodes.timetablemanager.db.DatabaseHelper
import com.peacecodes.timetablemanager.models.Authentication
import com.peacecodes.timetablemanager.util.Util

class SignUpFragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var navController: NavController
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        navController = NavHostFragment.findNavController(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initObjects()

        binding.Button.setOnClickListener {
            validateUserInputs()
        }
    }

    private fun initObjects() {
        databaseHelper = DatabaseHelper(requireActivity())
    }

    private fun validateUserInputs() {
        Util.hideKeyboard(requireActivity())
        binding.apply {
            when {
                fullName.text!!.isEmpty() -> {
                    Util.displayLongMessage(requireContext(), "Full name required.")
                    fullName.requestFocus()
                    return
                }
                reg.text!!.isEmpty() -> {
                    Util.displayLongMessage(
                        requireContext(),
                        "Please enter your registration number."
                    )
                    reg.requestFocus()
                    return
                }
                username.text!!.isEmpty() -> {
                    Util.displayLongMessage(requireContext(), "Please enter your username")
                    username.requestFocus()
                    return
                }
                passWord.text!!.isEmpty() -> {
                    Util.displayLongMessage(requireContext(), "Password required.")
                    passWord.requestFocus()
                    return
                }
                passWord.text.toString().length < 6 -> {
                    Util.displayLongMessage(
                        requireContext(),
                        "Your password should be at least 6 characters."
                    )
                    passWord.requestFocus()
                    return
                }
                passWord.text.toString() != confirm.text.toString() -> {
                    Util.displayLongMessage(requireContext(), "The two passwords does not match.")
                    confirm.requestFocus()
                    return
                }
                else -> {
                    Util.displayLongMessage(requireContext(), "Registration successful.")
                    navController.navigate(R.id.action_signUpFragment_to_signInFragment)
                    postDataToSQLite(binding)
                }
            }
        }
    }

    private fun postDataToSQLite(binding: FragmentSignUpBinding) {

        if (!databaseHelper.checkUser(binding.reg.text.toString().trim())) {
            val user = Authentication(
                full_name = binding.fullName.text.toString().trim(),
                reg_no = binding.reg.text.toString().trim(),
                username = binding.username.toString().trim(),
                password = binding.passWord.text.toString().trim()
            )
            databaseHelper.addUser(user)
            emptyInputEditText()
        }

    }
    private fun emptyInputEditText() {
        binding.fullName.text = null
        binding.username.text = null
        binding.reg.text = null
        binding.passWord.text = null
        binding.confirm.text = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}