package hr.ferit.patrikcupic.healthconnect.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hr.ferit.patrikcupic.healthconnect.AppointmentScheduler
import hr.ferit.patrikcupic.healthconnect.data.Appointment
import hr.ferit.patrikcupic.healthconnect.data.AppointmentStatus
import hr.ferit.patrikcupic.healthconnect.data.Doctor
import hr.ferit.patrikcupic.healthconnect.views.PatientViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScheduleScreen(
    navigation: NavController,
    patientViewModel: PatientViewModel,
) {
    var selectedDoctorId by remember { mutableStateOf("") }
    var selectedDoctorName by remember { mutableStateOf("Select Doctor") }
    var selectedDate by remember { mutableStateOf("Select Date") }
    var selectedTime by remember { mutableStateOf("Select Time") }
    var reason by remember { mutableStateOf("") }

    val doctorsData = patientViewModel.doctorsData
    val context = LocalContext.current

    Box(modifier = Modifier.fillMaxSize()) {

        Button(
            onClick = { navigation.navigateUp() },
            modifier = Modifier
                .padding(16.dp).align(Alignment.TopStart)
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
            Text("Schedule Appointment", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                showDatePickerDialog(context) { date -> selectedDate = date }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = selectedDate)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                showTimePickerDialog(context) { time -> selectedTime = time }
            }, modifier = Modifier.fillMaxWidth()) {
                Text(text = selectedTime)
            }

            Spacer(modifier = Modifier.height(16.dp))

            DoctorDropdownField(doctors = doctorsData, selectedDoctorName = selectedDoctorName) { doctorId, doctorName ->
                selectedDoctorId = doctorId
                selectedDoctorName = doctorName
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = reason,
                onValueChange = { reason = it },
                label = { Text("Reason for Appointment") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter reason") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedDoctorId.isNotEmpty() && selectedDate != "Select Date" && selectedTime != "Select Time" && reason.isNotEmpty()) {
                        val appointment = Appointment(
                            doctorId = selectedDoctorId,
                            reason = reason,
                            dateTime = "$selectedDate $selectedTime",
                            status = AppointmentStatus.PENDING,
                            doctorUsername = selectedDoctorName,
                        )
                        patientViewModel.scheduleAppointment(appointment, context)
                        navigation.navigateUp()
                    } else {
                        Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Schedule Appointment")
            }
        }
    }
}

@Composable
fun DoctorDropdownField(
    doctors: List<Doctor>,
    selectedDoctorName: String,
    onDoctorSelected: (String, String) -> Unit
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
                text = selectedDoctorName.ifEmpty { "Select Doctor" },
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
            if (doctors.isEmpty()) {
                Text("No doctors available", modifier = Modifier.padding(16.dp))
            } else {
                doctors.forEach { doctor ->
                    DropdownMenuItem(
                        text = { Text(text = "${doctor.username}, ${doctor.specialty}") },
                        onClick = {
                            onDoctorSelected(doctor.id, doctor.username)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

private fun showDatePickerDialog(context: android.content.Context, onDateSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
        val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
        onDateSelected(formattedDate)
    }, year, month, day).show()
}

private fun showTimePickerDialog(context: android.content.Context, onTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
        val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
        onTimeSelected(formattedTime)
    }, hour, minute, true).show()
}
