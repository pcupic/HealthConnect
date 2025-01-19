package hr.ferit.patrikcupic.healthconnect

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import hr.ferit.patrikcupic.healthconnect.ui.AppointmentScheduleScreen
import hr.ferit.patrikcupic.healthconnect.ui.DoctorDashboard
import hr.ferit.patrikcupic.healthconnect.ui.DoctorMedicalRecordsScreen
import hr.ferit.patrikcupic.healthconnect.ui.DoctorProfileScreen
import hr.ferit.patrikcupic.healthconnect.ui.LoginScreen
import hr.ferit.patrikcupic.healthconnect.ui.MedicalRecordFormScreen
import hr.ferit.patrikcupic.healthconnect.ui.PatientDashboard
import hr.ferit.patrikcupic.healthconnect.ui.PatientMedicalRecordsScreen
import hr.ferit.patrikcupic.healthconnect.ui.PatientProfileScreen
import hr.ferit.patrikcupic.healthconnect.ui.RegisterScreen
import hr.ferit.patrikcupic.healthconnect.views.DoctorViewModel
import hr.ferit.patrikcupic.healthconnect.views.PatientViewModel

object Routes {
    const val SCREEN_LOGIN = "login"
    const val SCREEN_REGISTER = "register"
    const val DASHBOARD_PATIENT = "dashboardPatient"
    const val DASHBOARD_DOCTOR = "dashboardDoctor"
    const val SCREEN_APPOINTMENT_SCHEDULE = "appointmentSchedule"
    const val SCREEN_PATIENT_PROFILE = "patientProfile"
    const val SCREEN_DOCTOR_PROFILE = "doctorProfile"
    const val SCREEN_DOCTOR_MEDICAL_RECORDS = "doctorMedicalRecords"
    const val SCREEN_MEDICAL_RECORD_FORM = "medicalRecordForm"
    const val SCREEN_PATIENT_MEDICAL_RECORDS = "patientMedicalRecords"
}

@Composable
fun NavigationController(
    patientViewModel: PatientViewModel,
    doctorViewModel: DoctorViewModel,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SCREEN_LOGIN) {
        composable(Routes.SCREEN_LOGIN) {
            LoginScreen(navController)
        }
        composable(Routes.SCREEN_REGISTER) {
            RegisterScreen(navController)
        }
        composable(Routes.DASHBOARD_PATIENT) {
            PatientDashboard(navController, patientViewModel)
        }
        composable(Routes.DASHBOARD_DOCTOR) {
            DoctorDashboard(navController, doctorViewModel)
        }
        composable(Routes.SCREEN_APPOINTMENT_SCHEDULE) {
            AppointmentScheduleScreen(navController, patientViewModel)
        }
        composable(Routes.SCREEN_PATIENT_PROFILE) {
            PatientProfileScreen(navController, patientViewModel)
        }
        composable(Routes.SCREEN_DOCTOR_PROFILE) {
            DoctorProfileScreen(navController, doctorViewModel)
        }
        composable(Routes.SCREEN_DOCTOR_MEDICAL_RECORDS) {
            DoctorMedicalRecordsScreen(navController, doctorViewModel)
        }
        composable(Routes.SCREEN_MEDICAL_RECORD_FORM) {
            MedicalRecordFormScreen(navController, doctorViewModel)
        }
        composable(Routes.SCREEN_PATIENT_MEDICAL_RECORDS) {
            PatientMedicalRecordsScreen(navController, patientViewModel)
        }
    }
}
