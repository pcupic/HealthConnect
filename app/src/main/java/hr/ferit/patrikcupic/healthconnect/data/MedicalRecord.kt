package hr.ferit.patrikcupic.healthconnect.data

data class MedicalRecord(
    var id: String = "",
    var patientId: String = "",
    var doctorId: String = "",
    var doctorUsername: String = "",
    var patientUsername: String = "",
    var details: String = "",
    var createdAt: String = "",
)

