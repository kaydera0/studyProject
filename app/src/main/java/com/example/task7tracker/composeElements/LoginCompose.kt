package com.example.task7tracker.composeElements

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.task7tracker.R
import com.example.task7tracker.composeElements.helpers.REGISTRATION_TAG
import com.example.task7tracker.composeElements.helpers.TRACKING_TAG
import com.example.task7tracker.viewModels.MainActivityViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun login(vm:MainActivityViewModel){

    val userName = remember {
        mutableStateOf(TextFieldValue())
    }
    val password = remember {
        mutableStateOf(TextFieldValue())
    }
    var passwordVisible = remember { mutableStateOf(false) }

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
            Text(text = stringResource(id = R.string.sign_in), color = Color.White, textAlign = TextAlign.Center, fontSize = 14.sp, fontWeight = FontWeight.Bold)
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
                            Text(
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
                            .padding(20.dp, 100.dp, 20.dp, 10.dp)
                            .height(70.dp)
                    )
                    TextField(
                        value = password.value,
                        onValueChange = {password.value = it},
                        label = {
                            Text(
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
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible.value)
                                Icons.Filled.Visibility
                            else Icons.Filled.VisibilityOff

                            val description = if (passwordVisible.value) stringResource(R.string.hide_pass) else stringResource(R.string.show_pass)

                            IconButton(onClick = {passwordVisible.value = !passwordVisible.value}){
                                Icon(imageVector  = image, description)
                            }
                        }
                    )
                    Text(text = stringResource(id = R.string.forgot_pass), color = Orange, fontWeight = FontWeight.Bold, modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp, 0.dp), textAlign =TextAlign.End)
                    Button(
                        shape = RoundedCornerShape(5.dp),
                        onClick = {
                            signIn(vm,userName.value.text,password.value.text)
                        },
                        colors = ButtonDefaults.buttonColors(
                            Orange
                        ),
                        enabled = when (userName.value.text.isNotEmpty() && password.value.text.isNotEmpty()) {
                            false-> false
                            else -> true
                        },
                        modifier = Modifier
                            .padding(
                                20.dp, 80.dp, 20.dp, 20.dp
                            )
                            .fillMaxWidth(1f)
                            .height(60.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_in),
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
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp)) {
                Text(text = stringResource(id = R.string.do_not_have_account), color = Color.White)
                Text(text = stringResource(id = R.string.sign_up), color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { vm.currentScreen.value=
                    REGISTRATION_TAG })
            }
        }
    }
}

fun signIn(vm: MainActivityViewModel,userName:String,password:String){

    val auth: FirebaseAuth =Firebase.auth

    auth.signInWithEmailAndPassword(userName, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                    vm.currentUserName.value = userName
                vm.currentScreen.value = TRACKING_TAG
            } else {
                vm.signInError.value = true
            }
        }
}