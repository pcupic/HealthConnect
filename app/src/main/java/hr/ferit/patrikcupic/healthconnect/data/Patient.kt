package hr.ferit.patrikcupic.healthconnect.data

data class Patient(
    var username: String = "",
    var id: String = "",
    val role: UserRole? = null
)
