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

class DoctorViewModel : ViewModel() {
    val appointmentsData = mutableStateListOf<Appointment>()
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


    private fun retrieveMedicalRecords() {
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

    private fun getDoctorUsername(onResult: (String?) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            db.collection("doctors")
                .document(currentUser.uid)
                .get()
                .addOnSuccessListener { document ->
                    val username = document.getString("username") ?: "Dr. Unknown"
                    onResult(username)
                }

        } else {
            onResult(null)
        }
    }

    fun addMedicalRecord(
        record: MedicalRecord,
    ) {
        val recordId = UUID.randomUUID().toString()

        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        getDoctorUsername { doctorUsername ->
            val recordData = hashMapOf(
                "id" to recordId,
                "patientId" to record.patientId,
                "doctorId" to record.doctorId,
                "doctorUsername" to doctorUsername,
                "details" to record.details,
                "createdAt" to currentDateTime,
                "patientUsername" to record.patientUsername,
            )

            db.collection("medical_records")
                .document(recordId)
                .set(recordData)
                .addOnSuccessListener {
                    retrieveMedicalRecords()
                }

        }
    }

    fun deleteMedicalRecord(
            record: MedicalRecord,
        ) {
            db.collection("medical_records")
                .document(record.id)
                .delete()
                .addOnSuccessListener {
                    medicalRecordsData.remove(record)
                }
        }

        fun deleteAppointment(appointment: Appointment) {
            db.collection("appointments")
                .document(appointment.id)
                .delete()
                .addOnSuccessListener {
                    appointmentsData.remove(appointment)
                }
        }

        fun updateData() {
            retrievePatients()
            retrieveAppointments()
            retrieveMedicalRecords()
        }
}

