package hr.ferit.patrikcupic.healthconnect.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel

@Composable
fun DoctorDashboard(
    navigation: NavController,
    doctorViewModel: DoctorViewModel,
) {
    val scheduledAppointments = doctorViewModel.appointmentsData
    LaunchedEffect(Unit) {
        doctorViewModel.updateData()
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .background(Color(0xFFF5F5F5)) // Light background for contrast
    ) {
        Text(
            text = "Doctor Dashboard",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { navigation.navigate(Routes.SCREEN_DOCTOR_MEDICAL_RECORDS) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Medical Records", color = Color.White)
            }
            Button(
                onClick = { navigation.navigate(Routes.SCREEN_DOCTOR_PROFILE) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
            ) {
                Text("Go to Profile", color = Color.White)
            }
            Button(
                onClick = { doctorViewModel.updateData() },
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
                    DoctorAppointmentItem(
                        appointment = appointment,
                        onConfirm = {
                            doctorViewModel.updateAppointmentStatus(appointment, AppointmentStatus.CONFIRMED)
                            doctorViewModel.updateData()
                        },
                        onCancel = {
                            doctorViewModel.updateAppointmentStatus(appointment, AppointmentStatus.CANCELED)
                            doctorViewModel.updateData()
                        },
                        onComplete = {
                            doctorViewModel.deleteAppointment(appointment)
                            doctorViewModel.updateData()
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
fun DoctorAppointmentItem(
    appointment: Appointment,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
    onComplete: () -> Unit,
) {

    val textColor = when (appointment.status) {
        AppointmentStatus.PENDING -> Color.Black
        AppointmentStatus.CONFIRMED -> Color(0xFF264440)
        AppointmentStatus.CANCELED -> Color.Red
        AppointmentStatus.COMPLETED -> Color.Gray
        else -> Color.Black
    }

    val borderColor = when (appointment.status) {
        AppointmentStatus.PENDING -> Color.Blue
        AppointmentStatus.CONFIRMED -> Color(0xFF264440)
        AppointmentStatus.CANCELED -> Color.Red
        AppointmentStatus.COMPLETED -> Color.Gray
        else -> Color.Transparent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
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
                    "Patient: ${appointment.patientUsername}",
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

            Column {
                Button(
                    onClick = {
                        onConfirm()
                    },
                    enabled = appointment.status == AppointmentStatus.PENDING,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text("Confirm", color = Color.White)
                }

                Button(
                    onClick = {
                        onCancel()
                    },
                    enabled = appointment.status != AppointmentStatus.CANCELED && appointment.status != AppointmentStatus.COMPLETED
                            && appointment.status != AppointmentStatus.CONFIRMED,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Cancel", color = Color.White)
                }

                Button(
                    onClick = {
                        onComplete()
                    },
                    enabled = appointment.status == AppointmentStatus.CONFIRMED,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D6B3F))
                ) {
                    Text("Complete", color = Color.White)
                }
            }
        }
    }
}