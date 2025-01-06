package hr.ferit.patrikcupic.healthconnect.data

data class Patient(
    var username: String = "",
    var id: String = "",
    var email: String? = null,
    var password: String? = null,
    val role: UserRole? = null
)
