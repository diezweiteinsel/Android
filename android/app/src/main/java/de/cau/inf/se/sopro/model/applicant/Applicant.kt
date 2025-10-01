package de.cau.inf.se.sopro.model.applicant

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

enum class Usertype{
    REPORTER,
    APPLICANT,
    ADMIN
}


@Entity
data class Applicant(
    @PrimaryKey
    @SerializedName("user_id")
    val userId: Int,
    val username: String,
    @SerializedName("created_at")
    val createdAt: String? = null,
    val role: Usertype,
    val email: String? = null,
    val jwt: String? = null
)