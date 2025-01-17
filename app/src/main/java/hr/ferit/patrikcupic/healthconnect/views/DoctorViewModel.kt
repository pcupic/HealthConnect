package hr.ferit.patrikcupic.healthconnect.views

import android.icu.text.SimpleDateFormat
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hr.ferit.patrikcupic.healthconnect.auth
import hr.ferit.patrikcupic.healthconnect.data.Appointment
import hr.ferit.patrikcupic.healthconnect.data.AppointmentStatus
import hr.ferit.patrikcupic.healthconnect.data.MedicalRecord
import hr.ferit.patrikcupic.healthconnect.data.Patient
import hr.ferit.patrikcupic.healthconnect.db
import java.util.Date
import java.util.Locale
import java.util.UUID

class DoctorViewModel : ViewModel(), AppointmentViewModel {
    override val appointmentsData = mutableStateListOf<Appointment>()
    val patientsData = mutableStateListOf<Patient>()
    val medicalRecordsData = mutableStateListOf<MedicalRecord>()

    init {
        updateData()
    }

    private fun retrieveAppointments() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("appointments")
                .whereEqualTo("doctorId", currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    appointmentsData.clear()  // Clear previous data
                    val appointments = result.documents.mapNotNull { document ->
                        document.toObject(Appointment::class.java)?.apply { id = document.id }
                    }
                    appointmentsData.addAll(appointments)
                }
        }
    }

    private fun retrievePatients() {
        db.collection("patients")
            .get()
            .addOnSuccessListener { result ->
                patientsData.clear()
                val patients = result.documents.mapNotNull { document ->
                    document.toObject(Patient::class.java)?.apply { id = document.id }
                }
                patientsData.addAll(patients)
            }
    }

    fun updateAppointmentStatus(appointment: Appointment, newStatus: AppointmentStatus) {
        db.collection("appointments").document(appointment.id)
            .update("status", newStatus.name)
            .addOnSuccessListener {
                appointment.status = newStatus
                updateData()
            }
    }

    fun retrieveMedicalRecords() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            db.collection("medical_records")
                .whereEqualTo("doctorId", currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    medicalRecordsData.clear()
                    val records = result.documents.mapNotNull { document ->
                        document.toObject(MedicalRecord::class.java)?.apply { id = document.id }
                    }
                    medicalRecordsData.addAll(records)
                }
        }
    }

        fun updateData() {
            retrievePatients()
            retrieveAppointments()
            retrieveMedicalRecords()
        }
}

