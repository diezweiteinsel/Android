package de.cau.inf.se.sopro.model.application

import androidx.room.Entity
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id", "formId"])
data class Application(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("form_id")
    val formId: Int,
    @SerializedName("admin_id")
    val adminId: Int?,
    val status: Status,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("currentSnapshotID")
    val currentSnapshotId: Int,
    @SerializedName("previousSnapshotID")
    val previousSnapshotId: Int,
    @SerializedName("is_public")
    val isPublic: Boolean,

    @SerializedName("jsonPayload")
    val dynamicAttributes: Map<String, JsonElement>? = emptyMap()
    )

enum class Status {
    PENDING,
    APPROVED,
    REJECTED
}
