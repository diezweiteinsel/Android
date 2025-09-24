package de.cau.inf.se.sopro.model.applicant

import kotlinx.serialization.Serializable
import java.util.Date
enum class Usertype{
    REPORTER,
    APPLICANT,
    ADMIN
}

@Serializable
data class Applicant(
    val userid: Int,
    val username: String,
    val password: String,
    //val createdAt: Date,
    val usertype: Usertype,
    val email: String? = null
)
