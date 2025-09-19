package de.cau.inf.se.sopro.model.application

import androidx.room.PrimaryKey

//First idea for a form class, still WIP
data class Form(
    @PrimaryKey
    val id: Int,
    val name: String,
    val title: String,
)