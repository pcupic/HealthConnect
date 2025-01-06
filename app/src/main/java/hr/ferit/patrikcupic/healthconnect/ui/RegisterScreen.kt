package hr.ferit.patrikcupic.healthconnect.ui

import hr.ferit.patrikcupic.healthconnect.authentication.Registration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.Routes
import hr.ferit.patrikcupic.healthconnect.data.Specialty

@Composable
fun RegisterScreen(navigation: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var userType by remember { mutableStateOf("Patient") }
    var bio by remember { mutableStateOf("") }
    var selectedSpecialty by remember { mutableStateOf("") }

    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(text = "Register", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(30.dp))

                UserTypeSelection(userType) { userType = it }
                Spacer(modifier = Modifier.height(20.dp))

                EmailInput(email) { email = it }
                Spacer(modifier = Modifier.height(10.dp))

                PasswordInput(password) { password = it }
                Spacer(modifier = Modifier.height(10.dp))

                UsernameInput(username) { username = it }
                Spacer(modifier = Modifier.height(20.dp))

                if (userType == "Doctor") {
                    BioInput(bio) { bio = it }
                    Spacer(modifier = Modifier.height(10.dp))
                    SpecialtyInput(selectedSpecialty) { selectedSpecialty = it }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                RegisterButton(userType, email, password, bio, selectedSpecialty, username, navigation)
            }
        }
    }
}


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
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
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
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(10.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BioInput(bio: String, onBioChange: (String) -> Unit) {
    OutlinedTextField(
        value = bio,
        onValueChange = onBioChange,
        label = { Text("Bio") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun SpecialtyInput(selectedSpecialty: String, onSpecialtyChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val specialtiesList = Specialty.values().toList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Select Specialty",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedSpecialty.ifEmpty { "Select an option" },
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                specialtiesList.forEach { specialty ->
                    DropdownMenuItem(
                        text = { Text(specialty.name.replace("_", " ")) },
                        onClick = {
                            onSpecialtyChange(specialty.name)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsernameInput(username: String, onUsernameChange: (String) -> Unit) {
    OutlinedTextField(
        value = username,
        onValueChange = onUsernameChange,
        label = { Text("Username") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
fun RegisterButton(
    userType: String,
    email: String,
    password: String,
    bio: String,
    specialty: String,
    username: String,
    navigation : NavController,
) {
    val context = LocalContext.current
    val registration = remember { Registration(context) }

    Button(
        onClick = {
            when (userType) {
                "Doctor" -> {
                    registration.registerDoctor(email, password, bio, specialty, username)
                }
                "Patient" -> {
                    registration.registerPatient(email, password, username)
                }
            }
            navigation.navigate(Routes.SCREEN_LOGIN)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = "Register", color = Color.White)
    }
}