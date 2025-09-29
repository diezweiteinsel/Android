package de.cau.inf.se.sopro.persistence

import androidx.room.TypeConverter
import de.cau.inf.se.sopro.model.application.Block
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString

class BConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromBlockMap(value: Map<String, Block>): String {
        return json.encodeToString(value)
    }

    @TypeConverter
    fun toBlockMap(value: String): Map<String, Block> {
        return json.decodeFromString(value)
    }
}