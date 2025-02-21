package hr.ferit.patrikcupic.healthconnect.views

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import hr.ferit.patrikcupic.healthconnect.AppointmentScheduler
import hr.ferit.patrikcupic.healthconnect.auth
import hr.ferit.patrikcupic.healthconnect.data.Appointment
import hr.ferit.patrikcupic.healthconnect.data.Doctor
import hr.ferit.patrikcupic.healthconnect.data.MedicalRecord
import hr.ferit.patrikcupic.healthconnect.db

class PatientViewModel : ViewModel() {
    val appointmentsData = mutableStateListOf<Appointment>()
    val doctorsData = mutableStateListOf<Doctor>()
    val medicalRecordsData = mutableStateListOf<MedicalRecord>()
    private val appointmentScheduler = AppointmentScheduler()

    init {
        updateData()
    }

    private fun retrieveMedicalRecords() {
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            db.collection("medical_records")
                .whereEqualTo("patientId", currentUserId)
                .get()
                .addOnSuccessListener { result ->
                    medicalRecordsData.clear()
                    result.documents.mapNotNull { document ->
                        document.toObject(MedicalRecord::class.java)?.apply { id = document.id }
                    }.let {
                        medicalRecordsData.addAll(it)
                    }
                }
        }
    }

    private fun retrieveAppointments() {
        val currentUserId = auth.currentUser?.uid

        if (currentUserId != null) {
            db.collection("appointments")
                .whereEqualTo("patientId", currentUserId)
                .get()
                .addOnSuccessListener { result ->
                    appointmentsData.clear()
                    result.documents.mapNotNull { data ->
                        data.toObject(Appointment::class.java)?.apply { id = data.id }
                    }.let {
                        appointmentsData.addAll(it)
                    }
                }
        }
    }

    private fun retrieveDoctors() {
        db.collection("doctors")
            .get()
            .addOnSuccessListener { result ->
                doctorsData.clear()
                result.documents.mapNotNull { data ->
                    data.toObject(Doctor::class.java)?.apply { id = data.id }
                }.let { doctorsData.addAll(it) }
            }
    }

    fun updateData() {
        retrieveDoctors()
        retrieveAppointments()
        retrieveMedicalRecords()
    }

    fun scheduleAppointment(appointment: Appointment, context: Context) {
        appointmentScheduler.scheduleAppointment(appointment, context)
    }

    fun deleteAppointment(appointment: Appointment) {
        appointmentScheduler.deleteAppointment(appointment)
    }
}

