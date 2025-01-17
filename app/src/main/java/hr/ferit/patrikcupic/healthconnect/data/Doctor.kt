package hr.ferit.patrikcupic.healthconnect.data

data class Doctor(
    var username: String = "",
    var id: String = "",
    var email: String = "",
    var password: String = "",
    var role: UserRole = UserRole.DOCTOR,
    var specialty: Specialty = Specialty.Pediatrics
)
