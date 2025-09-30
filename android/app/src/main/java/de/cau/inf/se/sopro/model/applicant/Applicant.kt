package de.cau.inf.se.sopro.model.applicant

import androidx.room.Entity
import androidx.room.PrimaryKey
import de.cau.inf.se.sopro.persistence.LocalDateTimeSerializer
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
    val userId: Int?,
    val username: String,
    val password: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime? = null,
    val role: Usertype,
    val email: String? = null,
    val jwt: String? = null
)