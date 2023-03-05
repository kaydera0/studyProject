package com.example.task7tracker.composeElements

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import com.example.task7tracker.composeElements.helpers.LOGIN_TAG
import com.example.task7tracker.composeElements.helpers.REGISTRATION_TAG
import com.example.task7tracker.composeElements.helpers.TRACKING_TAG
import com.example.task7tracker.viewModels.MainActivityViewModel

@Composable
fun ScreenContainer(vm:MainActivityViewModel){

    val screen: State<String> = vm.currentScreen.observeAsState(initial = LOGIN_TAG)

    when(screen.value){
        LOGIN_TAG -> login(vm)
        REGISTRATION_TAG -> registration(vm)
        TRACKING_TAG -> tracking(vm)
    }
}