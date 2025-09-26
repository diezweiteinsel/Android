package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import de.cau.inf.se.sopro.model.applicant.Applicant
//First idea for an application class, still WIP
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
    val applicantName: String,
    val submissionDate: String,
    val formId: Int,
    val status: Status,
    val public: Boolean,
    val edited: Boolean
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