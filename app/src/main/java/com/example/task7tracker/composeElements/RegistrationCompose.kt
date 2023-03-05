package com.example.task7tracker.composeElements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.task7tracker.R
import com.example.task7tracker.composeElements.helpers.LOGIN_TAG
import com.example.task7tracker.viewModels.MainActivityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun registration(vm: MainActivityViewModel){

    val userName = remember {
        mutableStateOf(TextFieldValue())
    }
    val password = remember {
        mutableStateOf(TextFieldValue())
    }
    val password2 = remember {
        mutableStateOf(TextFieldValue())
    }

    Column(
        Modifier
            .background(color = Orange)
            .fillMaxSize()){
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f),
            contentAlignment = Alignment.Center
        ){
            androidx.compose.material.Text(text = stringResource(id = R.string.sign_up), color = Color.White, textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .background(Orange)
                .padding(15.dp)
                .clip(shape = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp))
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)){
                Column() {
                    TextField(
                        singleLine = false,
                        value = userName.value,
                        onValueChange = {userName.value = it},
                        label = {
                            androidx.compose.material.Text(
                                text = stringResource(
                                    id = R.string.user_name
                                ), color = DarkGrey
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Grey
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 100.dp, 20.dp, 20.dp)
                            .height(70.dp)
                    )
                    TextField(
                        value = password.value,
                        onValueChange = {password.value = it},
                        label = {
                            androidx.compose.material.Text(
                                text = stringResource(
                                    id = R.string.password
                                ), color = DarkGrey
                            )
                        },colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Grey
                        ), modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 10.dp, 20.dp, 20.dp)
                            .height(70.dp),

                    )
                    TextField(
                        value = password2.value,
                        onValueChange = {password2.value = it},
                        label = {
                            androidx.compose.material.Text(
                                text = stringResource(
                                    id = R.string.password2
                                ), color = DarkGrey
                            )
                        },colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            containerColor = Grey
                        ), modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 10.dp, 20.dp, 20.dp)
                            .height(70.dp),

                        )

                    Button(
                        shape = RoundedCornerShape(5.dp),
                        onClick = {
                            signUp(vm, userName.value.text, password.value.text)
                        },
                        colors = ButtonDefaults.buttonColors(
                            Orange
                        ),
                        enabled = when (userName.value.text.isNotEmpty() && password.value.text.isNotEmpty() && password2.value.text.isNotEmpty()) {
                            false -> false
                            else -> true
                        },
                        modifier = Modifier
                            .padding(
                                20.dp, 80.dp, 20.dp, 20.dp
                            )
                            .fillMaxWidth(1f)
                            .height(60.dp)
                    ) {
                        androidx.compose.material.Text(
                            text = stringResource(id = R.string.sign_up),
                            color = Color.White,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .padding(vertical = 10.dp)
                                .fillMaxWidth(), textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
        Box(contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, start = 20.dp, end = 20.dp)) {
                androidx.compose.material.Text(text = stringResource(id = R.string.Notification), color = Color.White)
               }
        }
    }
}

fun signUp(vm: MainActivityViewModel, userName:String, password:String) {

    val auth = Firebase.auth
    auth.createUserWithEmailAndPassword(userName, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
            vm.currentScreen.value = LOGIN_TAG
            } else {
            vm.signInError.value = true
            }
        }
}