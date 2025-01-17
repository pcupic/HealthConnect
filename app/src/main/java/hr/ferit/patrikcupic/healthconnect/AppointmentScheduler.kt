package hr.ferit.patrikcupic.healthconnect

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import hr.ferit.patrikcupic.healthconnect.data.Appointment
import hr.ferit.patrikcupic.healthconnect.data.AppointmentStatus
import hr.ferit.patrikcupic.healthconnect.views.AppointmentViewModel
import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel
import hr.ferit.patrikcupic.healthconnect.views.PatientViewModel
import java.util.UUID

class AppointmentScheduler(
    private val context: Context,
) {
    fun scheduleAppointment(appointment: Appointment) {
        val currentUser = auth.currentUser
        val currentUserId = currentUser?.uid

        if (currentUserId != null) {
            db.collection("patients").document(currentUserId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val patientUsername = document.getString("username")
                        if (patientUsername != null) {
                            val appointmentId = UUID.randomUUID().toString()
                            val appointmentData = hashMapOf(
                                "appointmentId" to appointmentId,
                                "patientId" to currentUserId,
                                "doctorId" to appointment.doctorId,
                                "doctorUsername" to appointment.doctorUsername,
                                "patientUsername" to patientUsername,
                                "reason" to appointment.reason,
                                "dateTime" to appointment.dateTime,
                                "status" to appointment.status?.name,
                            )

                            db.collection("appointments")
                                .document(appointmentId)
                                .set(appointmentData)
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Appointment successfully scheduled", Toast.LENGTH_SHORT).show()
                                }

                        } else {
                            Toast.makeText(context, "Username not found for the current user", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "User document not found", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "User is not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    fun deleteAppointment(appointment: Appointment) {
        db.collection("appointments")
            .document(appointment.id)
            .delete()
    }

    fun updateAppointmentStatus(appointment: Appointment, newStatus: AppointmentStatus) {
        db.collection("appointments").document(appointment.id)
            .update("status", newStatus.name)
    }
}
