package de.cau.inf.se.sopro.model.applicant

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

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
    //val createdAt: LocalDateTime,
    val role: Usertype,
    val email: String? = null,
    val jwt: String? = ""
)
