import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.glory.wewenunua.data.AuthViewModel
import com.glory.wewenunua.navigation.ROUT_REGISTER
import com.glory.wewenunua.ui.theme.newYellow

@Composable
fun LoginScreen(navController: NavController){

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }



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
                text = "Welcome Back",
                fontSize = 36.sp,
                fontFamily = FontFamily.Cursive,
                color = newYellow,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Let’s get you signed in!",
                fontSize = 16.sp,
                color = Color.Gray
            )

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



            val context = LocalContext.current
            val authViewModel = AuthViewModel(navController, context)

            Row {
                Button(
                    onClick = {
                         authViewModel.login(email, password)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newYellow),

                    ) {
                    Text(text = "Login As User")
                }

                Spacer(modifier = Modifier.width(10.dp))

                Button(
                    onClick = {

                        authViewModel.adminlogin(email, password)

                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(newYellow),
                ) {
                    Text(text = "Login As Admin")
                }

            }

            Spacer(modifier = Modifier.height(10.dp))





            TextButton(onClick = {
                navController.navigate(ROUT_REGISTER)
            }) {
                Text(
                    text = "You don't have an account? Register",
                    color = Color.Blue
                )
            }






        }


    }




}

@Composable
@Preview(showBackground = true)
fun LoginScreenPreview(){
    LoginScreen(rememberNavController())
}