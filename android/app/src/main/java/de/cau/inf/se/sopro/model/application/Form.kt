package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

//First idea for a form class, still WIP
@Entity(tableName = "Form")
@Serializable
data class Form(
    @PrimaryKey
    val id: Int,
    val name: String,
    val title: String
)