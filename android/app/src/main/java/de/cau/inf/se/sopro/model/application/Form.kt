package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.cau.inf.se.sopro.persistence.BConverters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//First idea for a form class, still WIP
@Entity(tableName = "Form")
@Serializable
data class Form(
    @PrimaryKey
    val id: Int?,
    val form_name: String?, //can cast to formName
    @TypeConverters(BConverters::class)
    val blocks: Map<String, Block>?, //this list contains our building blocks for the application
    val is_Active: Boolean,
    val version: String?
)



@Serializable
data class Block( //this is one block
    val label: String, //this is the label of the block
    val data_type: String, //datatype of the block
    val required: Boolean,
    val constraintsJson: Constraints? = null //maybe we have to convert json: kotlinx.serialization.json.Json
)

@Serializable
data class Constraints(
    val min_length: Int? = null,
    val max_length: Int? = null
)