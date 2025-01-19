package hr.ferit.patrikcupic.healthconnect.views

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import hr.ferit.patrikcupic.healthconnect.AppointmentScheduler
import hr.ferit.patrikcupic.healthconnect.MedicalRecordManager
import hr.ferit.patrikcupic.healthconnect.auth
import hr.ferit.patrikcupic.healthconnect.data.Appointment
import hr.ferit.patrikcupic.healthconnect.data.AppointmentStatus
import hr.ferit.patrikcupic.healthconnect.data.MedicalRecord
import hr.ferit.patrikcupic.healthconnect.data.Patient
import hr.ferit.patrikcupic.healthconnect.db

class DoctorViewModel : ViewModel() {
    val appointmentsData = mutableStateListOf<Appointment>()
    val patientsData = mutableStateListOf<Patient>()
    val medicalRecordsData = mutableStateListOf<MedicalRecord>()
    private val appointmentScheduler = AppointmentScheduler()
    private val medicalRecordManager = MedicalRecordManager()

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
                    appointmentsData.clear()
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

        fun updateData() {
            retrievePatients()
            retrieveAppointments()
            retrieveMedicalRecords()
        }

    fun updateAppointmentStatus(appointment: Appointment, newStatus: AppointmentStatus) {
        appointmentScheduler.updateAppointmentStatus(appointment, newStatus)
    }

    fun deleteAppointment(appointment: Appointment) {
        appointmentScheduler.deleteAppointment(appointment)
    }

    fun addMedicalRecord(
        record: MedicalRecord,
    ) {
        medicalRecordManager.addMedicalRecord(record)
    }

    fun deleteMedicalRecord(
        record: MedicalRecord,
    ) {
        medicalRecordManager.deleteMedicalRecord(record)
    }
}

