package hr.ferit.patrikcupic.healthconnect.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(navController: NavController) { // Accept NavController as a parameter
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("Patient") } // Default user type

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Gumb za povratak na Login Screen
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = { navController.navigateUp() }) { // Navigate back to Login Screen
                    Text(text = "Back to Login")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "Register", fontSize = 30.sp)
            Spacer(modifier = Modifier.height(20.dp))

            UserTypeSelection(userType) { userType = it }

            Spacer(modifier = Modifier.height(20.dp))

            EmailInput(email) { email = it }
            Spacer(modifier = Modifier.height(10.dp))

            PasswordInput(password) { password = it }
            Spacer(modifier = Modifier.height(20.dp))

            RegisterButton(userType, email, password)
        }
    }
}

// ... (ostale funkcije ostaju nepromijenjene)


@Composable
fun UserTypeSelection(selectedUserType: String, onUserTypeChange: (String) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = (selectedUserType == "Doctor"),
            onClick = { onUserTypeChange("Doctor") },
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Text(text = "Doctor", modifier = Modifier.padding(start = 8.dp))

        Spacer(modifier = Modifier.width(16.dp))

        RadioButton(
            selected = (selectedUserType == "Patient"),
            onClick = { onUserTypeChange("Patient") },
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
        )
        Text(text = "Patient", modifier = Modifier.padding(start = 8.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmailInput(email: String, onEmailChange: (String) -> Unit) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Email") },
        modifier = Modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordInput(password: String, onPasswordChange: (String) -> Unit) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation()
    )
}

@Composable
fun RegisterButton(userType: String, email: String, password: String) {
    Button(onClick = {
        // Ovdje dodajte logiku za registraciju
        // Možete provjeriti userType i obraditi registraciju prema tome
    }, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Register")
    }
}
