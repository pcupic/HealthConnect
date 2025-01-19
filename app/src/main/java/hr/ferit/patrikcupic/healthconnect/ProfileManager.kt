@file:Suppress("DEPRECATION")

package hr.ferit.patrikcupic.healthconnect

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider

object ProfileManager {

    fun loadProfileData(
        role: String,
        onDataLoaded: (String, String) -> Unit
    ) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            db.collection(role).document(currentUserId).get()
                .addOnSuccessListener { document ->
                    val username = document.getString("username") ?: ""
                    val email = auth.currentUser?.email ?: ""
                    onDataLoaded(username, email)
                }
        }
    }

    fun updateProfile(
        context: Context,
        role: String,
        username: String,
        email: String,
        password: String
    ) {
        val currentUserId = auth.currentUser?.uid
        if (currentUserId != null) {
            updateUsername(context, role, currentUserId, username) {
                if (role == "doctors") {
                    updateDoctorUsernameInAppointmentsAndMedicalRecords(currentUserId, username)
                } else if (role == "patients") {
                    updatePatientUsernameInAppointmentsAndMedicalRecords(currentUserId, username)
                }

                updateEmail(context, email) {
                    updatePassword(context, password)
                }
            }
        } else {
            Toast.makeText(context, "User not logged in.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateUsername(
        context: Context,
        role: String,
        userId: String,
        username: String,
        onComplete: () -> Unit
    ) {
        db.collection(role).document(userId).update("username", username)
            .addOnSuccessListener {
                Toast.makeText(context, "Username updated.", Toast.LENGTH_SHORT).show()
                onComplete()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error updating username.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateEmail(
        context: Context,
        email: String,
        onComplete: () -> Unit
    ) {
        if (email.isNotEmpty() && email != auth.currentUser?.email) {
            auth.currentUser?.verifyBeforeUpdateEmail(email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Verify your email!", Toast.LENGTH_SHORT).show()
                        onComplete()
                    } else {
                        Toast.makeText(context, "Error updating email", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            onComplete()
        }
    }

    private fun updatePassword(
        context: Context,
        password: String
    ) {
        if (password.isNotEmpty()) {
            auth.currentUser?.updatePassword(password)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Password updated.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error updating password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    fun deletePatient(context: Context, password: String) {
        val currentUser = auth.currentUser
        val currentUserId = currentUser?.uid

        if (currentUserId != null) {
            val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
            currentUser.reauthenticate(credential).addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    db.collection("appointments").whereEqualTo("patientId", currentUserId).get()
                        .addOnSuccessListener { appointmentQuerySnapshot ->
                            if (!appointmentQuerySnapshot.isEmpty)
                                for (appointment in appointmentQuerySnapshot.documents)
                                    db.collection("appointments").document(appointment.id).delete()

                            db.collection("medical_records").whereEqualTo("patientId", currentUserId).get()
                                .addOnSuccessListener { medicalRecordQuerySnapshot ->
                                    if (!medicalRecordQuerySnapshot.isEmpty)
                                        for (medicalRecord in medicalRecordQuerySnapshot.documents)
                                            db.collection("medical_records").document(medicalRecord.id)
                                                .delete()

                                    db.collection("patients").document(currentUserId).delete()
                                        .addOnSuccessListener {
                                            currentUser.delete().addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Account and related data deleted successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to delete account: ${task.exception?.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                }
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Reauthentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, "No user is currently logged in.", Toast.LENGTH_SHORT).show()
        }
    }



    fun deleteDoctor(context: Context, password: String) {
        val currentUser = auth.currentUser
        val currentUserId = currentUser?.uid

        if (currentUserId != null) {
            val credential = EmailAuthProvider.getCredential(currentUser.email!!, password)
            currentUser.reauthenticate(credential).addOnCompleteListener { reAuthTask ->
                if (reAuthTask.isSuccessful) {
                    db.collection("appointments").whereEqualTo("doctorId", currentUserId).get()
                        .addOnSuccessListener { appointmentQuerySnapshot ->
                            for (appointment in appointmentQuerySnapshot.documents)
                                db.collection("appointments").document(appointment.id).delete()

                            db.collection("medical_records").whereEqualTo("doctorId", currentUserId).get()
                                .addOnSuccessListener { medicalRecordQuerySnapshot ->
                                    for (medicalRecord in medicalRecordQuerySnapshot.documents)
                                        db.collection("medical_records").document(medicalRecord.id).delete()

                                    db.collection("doctors").document(currentUserId).delete()
                                        .addOnSuccessListener {
                                            currentUser.delete().addOnCompleteListener { task ->
                                                if (task.isSuccessful) {
                                                    Toast.makeText(
                                                        context,
                                                        "Doctor account and related data deleted successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to delete account: ${task.exception?.message}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                }
                        }
                } else {
                    Toast.makeText(
                        context,
                        "Reauthentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            Toast.makeText(context, "No user is currently logged in.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateDoctorUsernameInAppointmentsAndMedicalRecords(
        doctorId: String,
        newUsername: String
    ) {
        db.collection("appointments")
            .whereEqualTo("doctorId", doctorId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val appointmentId = document.id
                    db.collection("appointments").document(appointmentId)
                        .update(
                            "doctorUsername",
                            newUsername
                        )
                }
            }

        db.collection("medical_records")
            .whereEqualTo("doctorId", doctorId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val medicalRecordId = document.id
                    db.collection("medical_records").document(medicalRecordId)
                        .update(
                            "doctorUsername",
                            newUsername
                        )
                }
            }
    }

    private fun updatePatientUsernameInAppointmentsAndMedicalRecords(
        patientId: String,
        newUsername: String
    ) {
        db.collection("appointments")
            .whereEqualTo("patientId", patientId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val appointmentId = document.id
                    db.collection("appointments").document(appointmentId)
                        .update("patientUsername", newUsername)
                }
            }

        db.collection("medical_records")
            .whereEqualTo("patientId", patientId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val medicalRecordId = document.id
                    db.collection("medical_records").document(medicalRecordId)
                        .update("patientUsername", newUsername)
                }
            }
    }

    fun handlePasswordReset(context: Context, email: String) {
        if (email.isNotEmpty()) {
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Reset password email sent.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(
                context,
                "Please enter your email to reset password.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
