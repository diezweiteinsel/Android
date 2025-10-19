package de.cau.inf.se.sopro.model.application

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "Form")
data class Form(
    @PrimaryKey
    val id: Int?,
    @ColumnInfo(name = "form_name")
    @SerializedName("form_name")
    val formName: String?, //can cast to formName
    val blocks: Map<String, Block>?, //this list contains our building blocks for the application
    @ColumnInfo(name = "is_active")
    @SerializedName("is_active")
    val isActive: Boolean,
    val version: String?
)


@Entity(tableName = "Blocks")
data class Block( //this is one block
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String, //this is the label of the block
    @SerializedName("data_type")
    val dataType: String, //datatype of the block
    val required: Boolean,
    val constraintsJson: Constraints? = null //maybe we have to convert json: kotlinx.serialization.json.Json
)

data class Constraints(
    @SerializedName("min_length")
    val minLength: Int? = null,
    @SerializedName("max_length")
    val maxLength: Int? = null
)