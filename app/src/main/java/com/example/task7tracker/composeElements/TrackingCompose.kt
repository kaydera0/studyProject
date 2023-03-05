package com.example.task7tracker.composeElements

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task7tracker.R
import com.example.task7tracker.composeElements.helpers.LOGIN_TAG
import com.example.task7tracker.viewModels.MainActivityViewModel

@Composable
fun tracking(vm: MainActivityViewModel) {

    val isTracking: State<Int> = vm.isTracking.observeAsState(initial = 0)

    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .clip(
                    RoundedCornerShape(0.dp, 0.dp, 10.dp, 10.dp)
                )
                .background(Orange)
        ) {
            Text(
                text = stringResource(id = R.string.tracker),
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(
                        Alignment.BottomCenter
                    )
                    .padding(bottom = 15.dp),
                textAlign = TextAlign.Center
            )
            Image(painterResource(id = R.drawable.baseline_exit_to_app_24),
                contentDescription = "",
                alignment = Alignment.BottomEnd,
                modifier = Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(15.dp)
                    .clickable { vm.currentScreen.value = LOGIN_TAG })
        }
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painterResource(
                    id = when (isTracking.value) {
                        0 -> R.drawable.point
                        1 -> R.drawable.point_gps_off
                        else -> R.drawable.point_active
                    }
                ), contentDescription = "", modifier = Modifier.align(Alignment.Center)
            )
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(150.dp)
                    .padding(bottom = 100.dp),
                strokeWidth = 12.dp,
                color = when (isTracking.value) {
                    2 -> Orange
                    else -> Color.Transparent
                },
            )
            Button(
                border = BorderStroke(4.dp, Orange),
                shape = RoundedCornerShape(5.dp),
                onClick = {
                    when (vm.networkStatus.value) {
                        true -> {
                            Log.d("DATA_BASE", "network tue in onClick")
                            Log.d(
                                "DATA_BASE",
                                "${vm.isTracking.value.toString()}   -- is tracking value"
                            )
                            if (isTracking.value == 1 || isTracking.value == 0) {
                                vm.isTracking.value = 2
                            } else {
                                vm.isTracking.value = 0
                            }
                        }
                        else -> vm.isTracking.value = 1
                    }
                },
                colors = when (isTracking.value) {
                    2 -> ButtonDefaults.buttonColors(Color.White)
                    else -> ButtonDefaults.buttonColors(Orange)
                },
                modifier = Modifier
                    .padding(
                        20.dp, 80.dp, 20.dp, 20.dp
                    )
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 50.dp)
                    .height(60.dp)
            ) {
                Text(
                    text = when (isTracking.value) {
                        2 -> stringResource(id = R.string.stop)
                        else -> stringResource(id = R.string.start)
                    },
                    color = when (isTracking.value) {
                        2 -> Orange
                        else -> Color.White
                    },
                    fontSize = 15.sp,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .fillMaxWidth(), textAlign = TextAlign.Center
                )
            }
        }
    }
}