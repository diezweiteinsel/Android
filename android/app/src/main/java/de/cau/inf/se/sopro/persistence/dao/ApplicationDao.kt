package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Upsert
import androidx.room.Query
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.application.Application
import kotlinx.coroutines.flow.Flow
@Dao
interface ApplicationDao {

    @Query("Select * from application where applicantId = :applicantId")
    fun getApplicantApplications(applicantId: Int) : Flow<List<Application>>

    @Query("Select * from application where public = 1")
    fun getPublicApplications() : Flow<List<Application>>

    @Insert
    suspend fun saveApplication(application: Application)

    @Upsert
    suspend fun updateApplication(application: Application)
}