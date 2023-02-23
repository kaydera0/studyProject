package com.example.task7tracker.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.example.task7tracker.R
import com.example.task7tracker.databinding.ActivityMainBinding
import com.example.task7tracker.viewModels.MainActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val vm: MainActivityViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var userName = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intentTracking = Intent(this, TrackingActivity::class.java)
        val intentRegistration = Intent(this, RegistrationActivity::class.java)
        auth = Firebase.auth

        binding.signInBtn.setOnClickListener {

            val userName = binding.userNameText.text.toString()
            val password = binding.passwordText.text.toString()
            auth.signInWithEmailAndPassword(userName, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        intentTracking.putExtra("userName", userName)
                        startActivity(intentTracking)
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
        binding.signUpTxt.setOnClickListener {
            startActivity(intentRegistration)
        }
        binding.userNameText.addTextChangedListener {
            userName = it.toString()
            vm.signInIsEnable.value = userName.isNotEmpty() && password.isNotEmpty()
        }
        binding.passwordText.addTextChangedListener {
            password = it.toString()
            vm.signInIsEnable.value = userName.isNotEmpty() && password.isNotEmpty()
        }
//        vm.signInIsEnable.observe(this, Observer {
//            if (it) {
//                binding.signInBtn.isEnabled = true
//                binding.signInBtn.background = getDrawable(R.drawable.btn_sign_enable)
//            } else {
//                binding.signInBtn.isEnabled = false
//                binding.signInBtn.background = getDrawable(R.drawable.btn_sign_disable)
//            }
//        })
    }
}