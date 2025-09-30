package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.persistence.LocalDateTimeSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

//First idea for an application class, still WIP
@Serializable //Lets us turn the attributes into a JSON
@Entity(foreignKeys = [
    ForeignKey(
        entity = Applicant::class,
        parentColumns = ["userId"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Application(
    @PrimaryKey
    val id: Int,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("form_id")
    val formId: Int,
    @SerialName("admin_id")
    val adminId: Int?,
    val status: Status,
    @Serializable(with = LocalDateTimeSerializer::class)
    @SerialName("created_at")
    val createdAt: LocalDateTime,
    @SerialName("currentSnapshotID")
    val currentSnapshotId: Int,
    @SerializedName("previousSnapshotID")
    val previousSnapshotId: Int,
    @SerialName("is_public")
    val isPublic: Boolean,

    val dynamicAttributes: Map<String, kotlinx.serialization.json.JsonElement> = emptyMap()
    )

enum class Status {
    PENDING,
    APPROVED,
    REJECTED
}
