package hr.ferit.patrikcupic.healthconnect.ui

import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.auth
import hr.ferit.patrikcupic.healthconnect.data.MedicalRecord
import hr.ferit.patrikcupic.healthconnect.data.Patient
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalRecordFormScreen(
    navigation: NavController,
    doctorViewModel: DoctorViewModel,
) {
    var selectedPatientId by remember { mutableStateOf("") }
    var selectedPatientName by remember { mutableStateOf("Select Patient") }
    var details by remember { mutableStateOf("") }

    val patientsData = doctorViewModel.patientsData
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create Medical Record", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            PatientDropdownField(patients = patientsData, selectedPatientName = selectedPatientName) { patientId, patientName ->
                selectedPatientId = patientId
                selectedPatientName = patientName
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = details,
                onValueChange = { details = it },
                label = { Text("Details") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter details for the medical record") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedPatientId.isNotEmpty() && details.isNotEmpty()) {
                        val medicalRecord = MedicalRecord(
                            patientId = selectedPatientId,
                            doctorId = auth.currentUser?.uid ?: "",
                            patientUsername = selectedPatientName,
                            details = details,
                            )
                        doctorViewModel.addMedicalRecord(medicalRecord)
                        Toast.makeText(context, "Medical Record Added", Toast.LENGTH_SHORT).show()
                        navigation.navigateUp()
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Medical Record")
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

@Composable
fun PatientDropdownField(
    patients: List<Patient>,
    selectedPatientName: String,
    onPatientSelected: (String, String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
                text = selectedPatientName.ifEmpty { "Select Patient" },
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
            if (patients.isEmpty()) {
                Text("No patients available", modifier = Modifier.padding(16.dp))
            } else {
                patients.forEach { patient ->
                    DropdownMenuItem(
                        text = { Text(patient.username) },
                        onClick = {
                            onPatientSelected(patient.id, patient.username)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}