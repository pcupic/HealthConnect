package hr.ferit.patrikcupic.healthconnect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel
import hr.ferit.patrikcupic.healthconnect.views.PatientViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val patientViewModel by viewModels<PatientViewModel>()
        val doctorViewModel by viewModels<DoctorViewModel>()
        setContent {
            NavigationController(patientViewModel, doctorViewModel)
        }
    }
}
