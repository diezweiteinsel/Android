package de.cau.inf.se.sopro.network.api

import com.google.gson.annotations.SerializedName
import de.cau.inf.se.sopro.ui.submitApplication.FieldPayload

data class UpdateApplicationRequest(
    @SerializedName("form_id")
    val formId: Int,
    @SerializedName("application_id")
    val applicationId: Int,
    @SerializedName("payload")
    val payload: Map<Int, FieldPayload>
)