package hr.ferit.patrikcupic.healthconnect

import android.annotation.SuppressLint
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@SuppressLint("StaticFieldLeak")
val db = FirebaseFirestore.getInstance()
val auth = FirebaseAuth.getInstance()
