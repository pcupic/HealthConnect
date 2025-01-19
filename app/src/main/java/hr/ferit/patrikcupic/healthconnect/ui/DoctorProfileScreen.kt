package hr.ferit.patrikcupic.healthconnect.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.ProfileManager
import hr.ferit.patrikcupic.healthconnect.Routes
import hr.ferit.patrikcupic.healthconnect.auth
import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileScreen(
    navigation: NavController,
    doctorViewModel: DoctorViewModel,
) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        ProfileManager.loadProfileData("doctors") { loadedUsername, loadedEmail ->
            username = loadedUsername
            email = loadedEmail
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { navigation.navigateUp() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Text("Back")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Doctor Profile",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    ProfileManager.updateProfile(
                        context = context,
                        role = "doctors",
                        username = username,
                        email = email,
                        password = password
                    )
                    doctorViewModel.updateData()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Profile")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    ProfileManager.signOut()
                    Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                    navigation.navigate(Routes.SCREEN_LOGIN)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sign Out")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (password.isNotEmpty()) {
                        ProfileManager.deleteDoctor(context, password)
                        doctorViewModel.updateData()
                        navigation.navigate(Routes.SCREEN_LOGIN)
                    } else {
                        Toast.makeText(context, "Please enter your password to confirm deletion", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Delete Account", color = Color.White)
            }
        }

        Button(
            onClick = { navigation.navigateUp() },
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.TopStart)
        ) {
            Text("Back")
        }
    }
}

