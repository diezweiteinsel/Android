package de.cau.inf.se.sopro.data

import android.content.Context
import de.cau.inf.se.sopro.model.applicant.Applicant
import kotlinx.serialization.json.Json
import okio.FileNotFoundException

class ApplicantRepository(private val context: Context) {

    private val fileName = "applicants_mock.json"

    fun getApplicant(): Applicant? {
        return try {
            val jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            val applicants = Json.decodeFromString<List<Applicant>>(jsonString)
            applicants.firstOrNull()
        } catch (e: FileNotFoundException) {
            null
        }
    }

    fun saveApplicant(applicant: Applicant) {

        val singleApplicantList = listOf(applicant)

        val jsonString = Json.encodeToString(singleApplicantList)

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(jsonString.toByteArray())
        }
    }
}