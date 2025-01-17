package hr.ferit.patrikcupic.healthconnect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.data.MedicalRecord
import hr.ferit.patrikcupic.healthconnect.views.PatientViewModel

@Composable
fun PatientMedicalRecordsScreen(
    navigation: NavController,
    patientViewModel: PatientViewModel,
) {
    val medicalRecords by remember { mutableStateOf(patientViewModel.medicalRecordsData) }

    LaunchedEffect(Unit) {
        patientViewModel.updateData()
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
                onClick = { navigation.navigateUp() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Back To Dashboard", color = Color.White)
            }
            Button(
                onClick = {
                    patientViewModel.updateData()
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
                    MedicalRecordCard(record)
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
fun MedicalRecordCard(record: MedicalRecord) {
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
        }
    }
}