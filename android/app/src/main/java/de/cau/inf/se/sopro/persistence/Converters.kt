package de.cau.inf.se.sopro.persistence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import java.time.LocalDateTime

class Converters {
    private val gson = Gson()

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
    fun fromStatus(value: String?): Status? {
        return value?.let { Status.valueOf(it) }
    }

    @TypeConverter
    fun toStatus(status: Status?): String? {
        return status?.name
    }

    @TypeConverter
    fun fromForm(value: String?): Form? {
        return value?.let { gson.fromJson(it, Form::class.java) }
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
}