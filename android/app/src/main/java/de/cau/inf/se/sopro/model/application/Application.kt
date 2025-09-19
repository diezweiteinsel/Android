package de.cau.inf.se.sopro.model.application

import androidx.room.PrimaryKey
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status

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