package de.cau.inf.se.sopro.persistence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import de.cau.inf.se.sopro.model.applicant.Usertype
import de.cau.inf.se.sopro.model.application.Block
import de.cau.inf.se.sopro.model.application.Constraints
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import java.time.LocalDateTime

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromStatus(status: Status?): String? {
        return status?.name
    }

    @TypeConverter
    fun toStatus(value: String?): Status? {
        return value?.let { Status.valueOf(it) }
    }

    @TypeConverter
    fun fromBlockMap(value: String?): Map<String, Block>? {
        if (value == null) {
            return null
        }
        val mapType = object : TypeToken<Map<String, Block>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun toBlockMap(map: Map<String, Block>?): String? {
        return gson.toJson(map)
    }

    @TypeConverter
    fun fromJsonElementMap(value: String?): Map<String, JsonElement>? {
        if (value == null) {
            return null
        }
        val mapType = object : TypeToken<Map<String, JsonElement>>() {}.type
        return Gson().fromJson(value, mapType)
    }

    @TypeConverter
    fun toJsonElementMap(map: Map<String, JsonElement>?): String? {
        if (map == null) {
            return null
        }
        return Gson().toJson(map)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }

    @TypeConverter
    fun toTimestamp(date: LocalDateTime?): String? {
        return date?.toString()
    }

    @TypeConverter
    fun fromUsertype(value: String?): Usertype? {
        return value?.let { Usertype.valueOf(it) }
    }

    @TypeConverter
    fun toUsertype(usertype: Usertype?): String? {
        return usertype?.name
    }

    @TypeConverter
    fun fromForm(value: String?): Form? {
        return value?.let { gson.fromJson(it, Form::class.java) }
    }

    @TypeConverter
    fun fromBlockList(jsonString: String?): List<Block>? {
        if (jsonString == null) {
            return null
        }
        val listType = object : TypeToken<List<Block>>() {}.type
        return gson.fromJson(jsonString, listType)
    }

    @TypeConverter
    fun toBlockList(blocks: List<Block>?): String? {
        return gson.toJson(blocks)
    }

    @TypeConverter
    fun toForm(form: Form?): String? {
        return gson.toJson(form)
    }

    @TypeConverter
    fun fromStringMap(value: String?): Map<String, String> {
        if (value == null)
            return emptyMap()

        val mapType = object : TypeToken<Map<String, String>>() {}.type

        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun toStringMap(map: Map<String, String>?): String {
        return gson.toJson(map ?: emptyMap<String, String>())
    }

    @TypeConverter
    fun fromConstraints(jsonString: String?): Constraints? {
        if (jsonString == null) {
            return null
        }
        return gson.fromJson(jsonString, Constraints::class.java)
    }

    @TypeConverter
    fun toConstraints(constraints: Constraints?): String? {
        return gson.toJson(constraints)
    }
}