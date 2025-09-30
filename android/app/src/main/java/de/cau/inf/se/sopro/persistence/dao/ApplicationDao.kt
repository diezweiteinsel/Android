package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.network.api.createApplication
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {

    @Query("Select * from Application where applicantId=:applicantId ")
    fun getApplicantApplications(applicantId: Int) : Flow<List<Application>>

    @Query("Select * from Application where isPublic = 1")
    fun getPublicApplications() : Flow<List<Application>>

    @Insert
    suspend fun saveApplication(application: Application)

    @Upsert
    suspend fun updateApplication(application: Application)
}