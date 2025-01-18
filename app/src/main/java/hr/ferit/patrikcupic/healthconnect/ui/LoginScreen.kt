package hr.ferit.patrikcupic.healthconnect.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.ProfileManager
import hr.ferit.patrikcupic.healthconnect.Routes
import hr.ferit.patrikcupic.healthconnect.authentication.Login

@Composable
fun LoginScreen(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Health Connect",
                fontSize = 40.sp,
                style = TextStyle(color = Color(0xFF4CAF50), fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                modifier = Modifier.padding(bottom = 40.dp)
            )

            val emailState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }

            EmailInput(emailState)
            Spacer(modifier = Modifier.height(10.dp))

            PasswordInput(passwordState)
            Spacer(modifier = Modifier.height(20.dp))

            LoginButton(emailState.value, passwordState.value, navController)
            Spacer(modifier = Modifier.height(10.dp))

            RegisterButton(navController)
            Spacer(modifier = Modifier.height(10.dp))

            ResetPasswordButton(emailState.value)
        }
    }
}

@Composable
fun LoginButton(email: String, password: String, navController: NavController) {
    val context = LocalContext.current
    val login = Login(context, navController)

    Button(
        onClick = {
            login.loginUser(
                email = email,
                password = password,
            )
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
    ) {
        Text(text = "Login", color = Color.White)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(emailState: MutableState<String>) {
    OutlinedTextField(
        value = emailState.value,
        onValueChange = { emailState.value = it },
        label = { Text(text = "Email") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(passwordState: MutableState<String>) {
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
    )
}

@Composable
fun RegisterButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(Routes.SCREEN_REGISTER) },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
    ) {
        Text(text = "Register", color = Color.White)
    }
}

@Composable
fun ResetPasswordButton(email: String) {
    val context = LocalContext.current

    Button(
        onClick = {
            ProfileManager.handlePasswordReset(context, email)
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
    ) {
        Text(text = "Forgot Password?", color = Color.White)
    }
}
