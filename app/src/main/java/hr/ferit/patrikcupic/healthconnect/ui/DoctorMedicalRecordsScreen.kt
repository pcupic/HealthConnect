package hr.ferit.patrikcupic.healthconnect.ui

import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.Routes
import hr.ferit.patrikcupic.healthconnect.data.MedicalRecord

@Composable
fun DoctorMedicalRecordsScreen(
    navigation: NavController,
    doctorViewModel: DoctorViewModel,
) {
    val medicalRecords by remember { mutableStateOf(doctorViewModel.medicalRecordsData) }

    LaunchedEffect(Unit) {
        doctorViewModel.updateData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5))
    ) {
        Text(
            text = "Medical Records",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { navigation.navigate(Routes.SCREEN_MEDICAL_RECORD_FORM) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Add New Record", color = Color.White)
            }
            Button(
                onClick = {
                    doctorViewModel.updateData()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Refresh", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (medicalRecords.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(medicalRecords) { record ->
                    MedicalRecordCard(record) {
                        doctorViewModel.deleteMedicalRecord(record)
                    }
                }
            }
        } else {
            Text(
                "No medical records available.",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}



@Composable
fun MedicalRecordCard(record: MedicalRecord, onDelete: (MedicalRecord) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                "Patient: ${record.patientUsername}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                "Details: ${record.details}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                "Created At: ${record.createdAt}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { onDelete(record) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Delete", color = Color.White)
            }
        }
    }
}

