package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import de.cau.inf.se.sopro.persistence.LocalDateTimeSerializer

//First idea for an application class, still WIP
@Serializable //Lets us turn the attributes into a JSON
@Entity(tableName = "Application")
data class Application(
    @PrimaryKey
    val id: Int,
    val applicantId: Int,
    val formId: Int,
    val applicantName: String,

    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,

    val status: Status,
    val isPublic: Boolean,
    val isEdited: Boolean,

    val dynamicAttributes: Map<String, String> = emptyMap()
    )

enum class Status {
    PENDING,
    APPROVED,
    REJECTED
}