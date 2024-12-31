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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Preview(showBackground = true)
@Composable
fun LoginScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Ovdje osiguravamo da Column zauzima cijeli prostor
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Login", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(20.dp))

            val emailState = remember { mutableStateOf("") }
            val passwordState = remember { mutableStateOf("") }

            EmailInput(emailState)
            Spacer(modifier = Modifier.height(10.dp))
            PasswordInput(passwordState)
            Spacer(modifier = Modifier.height(20.dp))

            LoginButton(emailState.value, passwordState.value)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(emailState: MutableState<String>) {
    OutlinedTextField(
        value = emailState.value,
        onValueChange = { emailState.value = it },
        label = { Text(text = "Email") },
        modifier = Modifier.fillMaxWidth()
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
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun LoginButton(email: String, password: String) {
    Button(onClick = {
        // Ovdje dodajte logiku za prijavu
    }, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Login")
    }
}