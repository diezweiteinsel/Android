package de.cau.inf.se.sopro.model.applicant

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Date
enum class Usertype{
    REPORTER,
    APPLICANT,
    ADMIN
}

@Serializable
@Entity
data class Applicant(
    @PrimaryKey
    val userid: Int,
    val username: String,
    val password: String,
    //val createdAt: Date,
    val role: Usertype,
    val email: String? = null,
    val jwt: String? = ""
)