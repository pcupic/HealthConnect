@file:Suppress("DEPRECATION")

package hr.ferit.patrikcupic.healthconnect

import android.content.Context
import android.widget.Toast

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
            auth.currentUser?.updateEmail(email)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onComplete()
                    } else {
                        Toast.makeText(context, "Error updating email.", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error updating password.", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    fun deletePatient(context: Context) {
        val currentUserId = auth.currentUser?.uid

        currentUserId?.let { id ->
            db.collection("appointments").whereEqualTo("patientId", id).get()
                .addOnSuccessListener { appointmentQuerySnapshot ->
                    if (!appointmentQuerySnapshot.isEmpty)
                        for (appointment in appointmentQuerySnapshot.documents)
                            db.collection("appointments").document(appointment.id).delete()

                    db.collection("medical_records").whereEqualTo("patientId", id).get()
                        .addOnSuccessListener { medicalRecordQuerySnapshot ->
                            if (!medicalRecordQuerySnapshot.isEmpty)
                                for (medicalRecord in medicalRecordQuerySnapshot.documents)
                                    db.collection("medical_records").document(medicalRecord.id).delete()


                            db.collection("patients").document(id).delete()
                                .addOnSuccessListener {
                                    // Now delete the user account
                                    auth.currentUser?.delete()?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Account and related data deleted successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Failed to delete account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        }
                }
        } ?: run {
            Toast.makeText(context, "No user is currently logged in.", Toast.LENGTH_SHORT).show()
        }
    }


    fun deleteDoctor(context: Context) {
        val currentUserId = auth.currentUser?.uid

        currentUserId?.let { id ->
            db.collection("appointments").whereEqualTo("doctorId", id).get()
                .addOnSuccessListener { appointmentQuerySnapshot ->
                    for (appointment in appointmentQuerySnapshot.documents)
                        db.collection("appointments").document(appointment.id).delete()


                    db.collection("medical_records").whereEqualTo("doctorId", id).get()
                        .addOnSuccessListener { medicalRecordQuerySnapshot ->
                            for (medicalRecord in medicalRecordQuerySnapshot.documents)
                                db.collection("medical_records").document(medicalRecord.id).delete()

                            db.collection("doctors").document(id).delete()
                                .addOnSuccessListener {
                                    auth.currentUser?.delete()?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "Doctor account and related data deleted successfully", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Failed to delete account: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                        }
                }
        } ?: run {
            Toast.makeText(context, "No user is currently logged in.", Toast.LENGTH_SHORT).show()
        }
    }


}
