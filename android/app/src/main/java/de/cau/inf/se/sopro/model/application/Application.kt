package de.cau.inf.se.sopro.model.application

import androidx.room.PrimaryKey

//First idea for an application class, still WIP
data class Application(
    @PrimaryKey
    val id: Int,
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