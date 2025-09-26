package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

//First idea for an application class, still WIP
@Entity(tableName = "") //TODO
data class Application(
    @PrimaryKey
    val id: Int,
    val applicantId: Int,
    val applicantName: String,
    val createdAt: LocalDateTime,
    val form: Form,
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