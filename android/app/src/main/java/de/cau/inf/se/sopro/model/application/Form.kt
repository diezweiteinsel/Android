package de.cau.inf.se.sopro.model.application

import androidx.room.PrimaryKey

data class Form(
    @PrimaryKey
    val id: Int,
    val name: String,
    val title: String,
)