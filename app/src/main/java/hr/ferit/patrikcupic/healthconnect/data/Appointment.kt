package hr.ferit.patrikcupic.healthconnect.data

data class Appointment(
    var id: String = "",
    var patientId: String? = null,
    var patientUsername : String = "",
    var doctorUsername : String = "",
    var doctorId: String = "",
    var dateTime: String = "",
    var reason: String = "",
    var status : AppointmentStatus? = null,
)
