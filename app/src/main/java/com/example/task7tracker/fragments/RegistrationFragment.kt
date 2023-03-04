package com.example.task7tracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.task7tracker.R
import com.example.task7tracker.databinding.FragmentRegistrationBinding
import com.example.task7tracker.viewModels.RegistrationViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment @Inject constructor() : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel = RegistrationViewModel()
    private var userName = ""
    private var password = ""
    private var password2 = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater)

        binding.signUpBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context, getString(R.string.success),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
                    } else {
                        Toast.makeText(
                            context, getString(R.string.something_wrong),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        binding.userNameTextRegistration.addTextChangedListener {
            if (it?.length!! > 0) {
                userName = it.toString()
                viewModel.signUpIsEnable.value =
                    userName.isNotEmpty() && password == password2 && password.length > 5
            }
        }
        binding.passwordTextRegistration.addTextChangedListener {
            if (it?.length!! > 0) {
                password = it.toString()
                viewModel.signUpIsEnable.value =
                    userName.isNotEmpty() && password == password2 && password.length > 5
            }
        }
        binding.passwordText2Registration.addTextChangedListener {
            if (it?.length!! > 0) {
                password2 = it.toString()
                viewModel.signUpIsEnable.value =
                    userName.isNotEmpty() && password == password2 && password.length > 5
            }
        }
        viewModel.signUpIsEnable.observe(viewLifecycleOwner, Observer {
            binding.signUpBtn.isEnabled = it == true
            if (it) {
                binding.signUpBtn.background = ResourcesCompat.getDrawable(
                    requireActivity().resources, R.drawable.btn_sign_enable, null
                )
            } else {
                binding.signUpBtn.background = ResourcesCompat.getDrawable(
                    requireActivity().resources, R.drawable.btn_sign_disable, null
                )
            }
        })
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}