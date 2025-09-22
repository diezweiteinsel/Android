package de.cau.inf.se.sopro.model.Applicant

import java.util.Date
enum class Usertype{
    REPORTER,
    APPLICANT,
    ADMIN
}
data class Applicant(
    val username: String,
    val password: String,
    val createdAt: Date,
    val usertype: Usertype,
    val email: String
)
