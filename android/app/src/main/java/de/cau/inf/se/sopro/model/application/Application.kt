package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.cau.inf.se.sopro.persistence.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

import androidx.room.TypeConverter
import de.cau.inf.se.sopro.model.applicant.Applicant
//First idea for an application class, still WIP
@Serializable //Lets us turn the attributes into a JSON
@Entity(foreignKeys = [
    ForeignKey(
        entity = Applicant::class,
        parentColumns = ["userid"],
        childColumns = ["applicantId"],
        onDelete = ForeignKey.CASCADE
    )
])
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

class Converters {
    @TypeConverter
    fun fromStatus(value: Status): String = value.name

    @TypeConverter
    fun toStatus(value: String): Status = Status.valueOf(value)
}