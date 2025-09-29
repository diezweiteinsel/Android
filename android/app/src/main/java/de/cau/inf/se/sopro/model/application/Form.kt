package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import de.cau.inf.se.sopro.persistence.BlockListSerializer
import de.cau.inf.se.sopro.persistence.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

//First idea for a form class, still WIP
@Entity(tableName = "Form")
@Serializable
data class Form(
    @PrimaryKey
    val id: Int,
    val name: String,
    val title: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    //@Serializable(with = BlockListSerializer::class)
    val sections: List<Block> = emptyList() //this list contains our building blocks for the application
)



@Serializable
data class Block( //this is one block
    val id: String,
    val name: String, //this is the label of the block
    val type: String, //datatype of the block
    val value: String
)