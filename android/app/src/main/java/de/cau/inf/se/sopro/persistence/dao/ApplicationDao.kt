package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.network.api.createApplication
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {

    @Query("Select * from Application where userId=:userId ")
    fun getApplicantApplications(userId: Int) : Flow<List<Application>>

    @Query("SELECT * FROM Application WHERE userId = :userId")
    fun getApplicationsAsFlow(userId: Int): Flow<List<Application>>

    @Query("Select * from Application where isPublic = 1")
    fun getPublicApplicationsAsFlow() : Flow<List<Application>>

    @Query("DELETE FROM Application")
    suspend fun deleteAll()

    @Upsert
    suspend fun upsertAll(applications: List<Application>)

    @Insert
    suspend fun saveApplication(application: Application)
}