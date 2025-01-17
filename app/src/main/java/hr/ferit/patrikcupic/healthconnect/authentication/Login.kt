package hr.ferit.patrikcupic.healthconnect.authentication

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.ferit.patrikcupic.healthconnect.auth
import hr.ferit.patrikcupic.healthconnect.Routes
import hr.ferit.patrikcupic.healthconnect.db

class Login(private val context: Context, private val navigation: NavController) {

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "All fields must be filled.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    checkUserRole()
                } else {
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun checkUserRole() {
        val user = auth.currentUser
        user?.let {
            db.collection("doctors").document(it.uid)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        navigation.navigate(Routes.DASHBOARD_DOCTOR)
                    } else {
                        db.collection("patients").document(it.uid)
                            .get()
                            .addOnSuccessListener { patientDoc ->
                                if (patientDoc.exists()) {
                                    navigation.navigate(Routes.DASHBOARD_PATIENT)
                                } else {
                                    Toast.makeText(context, "User not found", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                    }
                }
        }
    }
}

