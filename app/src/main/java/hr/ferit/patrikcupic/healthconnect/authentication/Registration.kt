package hr.ferit.patrikcupic.healthconnect.authentication

import android.content.Context
import android.widget.Toast
import hr.ferit.patrikcupic.healthconnect.auth
import hr.ferit.patrikcupic.healthconnect.data.UserRole
import hr.ferit.patrikcupic.healthconnect.db

class Registration(private val context: Context) {
    private fun savePatientToDatabase(uid: String, userData: Map<String, Any>) {
        db.collection("patients").document(uid)
            .set(userData)
    }

    private fun saveDoctorToDatabase(uid: String, userData: Map<String, Any>) {
        db.collection("doctors").document(uid)
            .set(userData)
    }

    fun registerDoctor(email: String, password: String, specialty: String, username : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: return@addOnCompleteListener

                    val doctorData = mapOf(
                        "id" to uid,
                        "role" to UserRole.DOCTOR.name,
                        "specialty" to specialty,
                        "username" to username
                    )

                    saveDoctorToDatabase(uid, doctorData)
                    Toast.makeText(context, "Doctor registered successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Doctor registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun registerPatient(email: String, password: String, username : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = task.result?.user?.uid ?: return@addOnCompleteListener

                    val patientData = mapOf(
                        "id" to uid,
                        "role" to UserRole.PATIENT.name,
                        "username" to username
                    )

                    savePatientToDatabase(uid, patientData)
                    Toast.makeText(context, "Patient registered successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Patient registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
