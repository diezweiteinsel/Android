package de.cau.inf.se.sopro.persistence

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.cau.inf.se.sopro.model.application.Block
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Status
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
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
    fun fromBlockList(blocks: List<Block>?): String? {
        return blocks?.let { Json.encodeToString(BlockListSerializer, it) }
    }

    @TypeConverter
    fun toBlockList(jsonString: String?): List<Block>? {
        return jsonString?.let { Json.decodeFromString(BlockListSerializer, it) }
    }
    /*
    @TypeConverter
    fun fromBlockList(value: List<Block>?): String {
        return gson.toJson(value)
    }
    @TypeConverter
    fun toBlockList(value: String): List<Block>? {
        val listType = object : TypeToken<List<Block>>() {}.type
        return gson.fromJson(value, listType)
    }*/
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

object BlockListSerializer : KSerializer<List<Block>> {

    override val descriptor: SerialDescriptor = ListSerializer(Block.serializer()).descriptor

    override fun serialize(encoder: Encoder, value: List<Block>) {
        val json = Json.encodeToString(ListSerializer(Block.serializer()), value)
        encoder.encodeString(json)
    }
    override fun deserialize(decoder: Decoder): List<Block> {
        val json = decoder.decodeString()
        return Json.decodeFromString(ListSerializer(Block.serializer()), json)
    }

}
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {

        encoder.encodeString(value.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString())
    }
}