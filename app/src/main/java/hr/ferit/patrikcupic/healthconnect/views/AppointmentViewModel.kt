package hr.ferit.patrikcupic.healthconnect.views

import hr.ferit.patrikcupic.healthconnect.data.Appointment

interface AppointmentViewModel {
    val appointmentsData: MutableList<Appointment>
}
