package com.example.task7tracker.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.task7tracker.R
import com.example.task7tracker.composeElements.ScreenContainer
import com.example.task7tracker.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivityCompose : AppCompatActivity() {

    private val vm: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreenContainer(vm)
        }

        vm.signInError.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this, getString(R.string.authentication_failed),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        vm.isTracking.observe(this, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                while (it == 2) {
                    vm.getDeviceLocation(vm.currentUserName.value!!)
                    delay(5000)
                }
            }
        })
        vm.currentUserName.observe(this, Observer {
            if (it.isNotEmpty()) {
                vm.sendLocationsFromDBToFirebase(it)
            }
        })
    }
}