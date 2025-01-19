package hr.ferit.patrikcupic.healthconnect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.Routes
import hr.ferit.patrikcupic.healthconnect.data.Appointment
import hr.ferit.patrikcupic.healthconnect.data.AppointmentStatus
import hr.ferit.patrikcupic.healthconnect.views.PatientViewModel

@Composable
fun PatientDashboard(
    navigation: NavController,
    patientViewModel: PatientViewModel,
) {
    LaunchedEffect(Unit) {
        patientViewModel.updateData()
    }

    val scheduledAppointments = patientViewModel.appointmentsData

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color(0xFFF5F5F5))
    ) {
        Text(
            text = "Patient Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { navigation.navigate(Routes.SCREEN_APPOINTMENT_SCHEDULE) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Schedule Appointment", color = Color.White)
            }
            Button(
                onClick = { navigation.navigate(Routes.SCREEN_PATIENT_MEDICAL_RECORDS) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Medical Records", color = Color.White)
            }
            Button(
                onClick = { navigation.navigate(Routes.SCREEN_PATIENT_PROFILE) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Go to Profile", color = Color.White)
            }
            Button(
                onClick = { patientViewModel.updateData() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Refresh", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (scheduledAppointments.isNotEmpty()) {
            Text("Scheduled Appointments", style = MaterialTheme.typography.titleLarge)

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(scheduledAppointments) { appointment ->
                    PatientAppointmentItem(
                        appointment = appointment,
                        onDelete = { appointmentToDelete ->
                            patientViewModel.deleteAppointment(appointmentToDelete)
                            patientViewModel.updateData()
                        }
                    )
                }
            }
        } else {
            Text("No scheduled appointments", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun PatientAppointmentItem(
    appointment: Appointment,
    onDelete: (Appointment) -> Unit,
) {
    val backgroundColor = when (appointment.status) {
        AppointmentStatus.PENDING -> Color(0xFF1E1E1E)
        AppointmentStatus.CONFIRMED -> Color(0xFF388E3C)
        AppointmentStatus.CANCELED -> Color(0xFFD32F2F)
        AppointmentStatus.COMPLETED -> Color(0xFF616161)
        else -> Color.White
    }

    val textColor = if (appointment.status == AppointmentStatus.PENDING) Color.White else Color.Black

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Doctor: ${appointment.doctorUsername}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
                Text(
                    "Date & Time: ${appointment.dateTime}",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor
                )
                Text(
                    "Reason: ${appointment.reason}",
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor
                )
                Text(
                    "Status: ${appointment.status}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { onDelete(appointment) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                modifier = Modifier.align(Alignment.CenterVertically),
                enabled = appointment.status == AppointmentStatus.CANCELED || appointment.status == AppointmentStatus.PENDING
            ) {
                Text("Delete", color = Color.White)
            }
        }
    }
}

