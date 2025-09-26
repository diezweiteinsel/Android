package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

//First idea for an application class, still WIP
@Entity()
data class Application(
    @PrimaryKey
    val id: Int,
    val applicantId: Int,
    val applicantName: String,
    val submissionDate: String,
    val form: Form,
    val status: Status,
    val public: Boolean,
    val edited: Boolean
    )

enum class Status {
    PENDING,
    APPROVED,
    REJECTED
}