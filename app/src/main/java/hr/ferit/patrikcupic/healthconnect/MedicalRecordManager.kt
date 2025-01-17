package hr.ferit.patrikcupic.healthconnect

import android.icu.text.SimpleDateFormat
import hr.ferit.patrikcupic.healthconnect.data.MedicalRecord
import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel
import java.util.Date
import java.util.Locale
import java.util.UUID

class MedicalRecordManager(
    private val doctorViewModel: DoctorViewModel,
) {

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
                    doctorViewModel.updateData()
                }

        }
    }

    fun deleteMedicalRecord(
        record: MedicalRecord,
    ) {
        db.collection("medical_records")
            .document(record.id)
            .delete()
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
}