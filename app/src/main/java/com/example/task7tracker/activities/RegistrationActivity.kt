package com.example.task7tracker.activities


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.task7tracker.R
import com.example.task7tracker.databinding.ActivityRegistrationBinding
import com.example.task7tracker.viewModels.RegistrationViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel = RegistrationViewModel()
    private var userName = ""
    private var password = ""
    private var password2 = ""

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signUpBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.createUserWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "success",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this, "something wrong",
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
        viewModel.signUpIsEnable.observe(this, Observer {
            binding.signUpBtn.isEnabled = it == true
            if (it) {
                binding.signUpBtn.background = getDrawable(R.drawable.btn_sign_enable)
            } else {
                binding.signUpBtn.background = getDrawable(R.drawable.btn_sign_disable)
            }
        })
    }
}