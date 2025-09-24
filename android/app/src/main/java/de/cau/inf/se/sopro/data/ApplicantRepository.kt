package de.cau.inf.se.sopro.data

import android.content.Context
import de.cau.inf.se.sopro.model.applicant.Applicant
import kotlinx.serialization.json.Json
import okio.FileNotFoundException

class ApplicantRepository(private val context: Context) {

    private val fileName = "applicants_mock.json"

    fun getApplicants(): MutableList<Applicant> {
        return try {
            val jsonString = context.openFileInput(fileName).bufferedReader().use { it.readText() }
            Json.decodeFromString<MutableList<Applicant>>(jsonString)
        } catch (e: FileNotFoundException) {
            mutableListOf()

        }
    }

    fun saveApplicant(applicant: Applicant) {
        val applicants = getApplicants()
        applicants.add(applicant)

        val updateJsonString = Json.encodeToString(applicants)

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(updateJsonString.toByteArray())
        }
    }
}