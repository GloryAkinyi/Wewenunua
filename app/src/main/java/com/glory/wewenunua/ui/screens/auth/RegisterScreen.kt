package com.glory.wewenunua.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.glory.wewenunua.R
import com.glory.wewenunua.data.AuthViewModel
import com.glory.wewenunua.navigation.ROUT_LOGIN
import com.glory.wewenunua.ui.theme.newYellow

@Composable
fun RegisterScreen(navController: NavController){

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }



    Column(
        modifier = Modifier
            .fillMaxSize()
        //  .paint(painter = painterResource(id= R.drawable.img_3), contentScale = ContentScale.Crop)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ){


        Spacer(modifier = Modifier.height(40.dp))


            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){


                Text(
                    text = "Megamart",
                    fontSize = 40.sp,
                    fontFamily = FontFamily.Cursive,
                    color = newYellow,
                    fontWeight = FontWeight.ExtraBold
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Join us and start your journey today",
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.height(10.dp))


                //Username
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.width(350.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = newYellow,
                        unfocusedLeadingIconColor = newYellow
                    ),
                    leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "") },
                    label = {  Text(text = "Username") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
                //End

                Spacer(modifier = Modifier.height(10.dp))

                //Email
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.width(350.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = newYellow,
                        unfocusedLeadingIconColor = newYellow
                    ),
                    leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "") },
                    label = {  Text(text = "Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                //End

                Spacer(modifier = Modifier.height(10.dp))

                //Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.width(350.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = newYellow,
                        unfocusedLeadingIconColor = newYellow
                    ),
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
                    label = {  Text(text = "Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                //End

                Spacer(modifier = Modifier.height(10.dp))


                //Confirm Password
                OutlinedTextField(
                    value = confirmpassword,
                    onValueChange = { confirmpassword = it },
                    modifier = Modifier.width(350.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = newYellow,
                        unfocusedLeadingIconColor = newYellow
                    ),
                    leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "") },
                    label = {  Text(text = "Confirm Password") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation()
                )
                //End

                Spacer(modifier = Modifier.height(10.dp))



                val context = LocalContext.current
                val authViewModel = AuthViewModel(navController, context)
                Button(
                    onClick = {

                        authViewModel.signup(username, email, password,confirmpassword)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newYellow),
                    modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)
                ) {
                    Text(text = "Create An Account")
                }

                Spacer(modifier = Modifier.height(10.dp))

                TextButton(onClick = {
                    navController.navigate(ROUT_LOGIN)
                }) {
                    Text(
                        text = "Already have an account? Login",
                        color = Color.Blue
                    )
                }







            }


        }




}

@Composable
@Preview(showBackground = true)
fun RegisterScreenPreview(){
    RegisterScreen(rememberNavController())
}