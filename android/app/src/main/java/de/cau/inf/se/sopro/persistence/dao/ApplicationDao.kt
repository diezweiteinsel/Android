package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import de.cau.inf.se.sopro.model.application.Application
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

    @Transaction
    suspend fun clearAndUpsertPublic(publicApplications: List<Application>) {
        deletePublic()
        upsertAll(publicApplications)
    }

    @Transaction
    suspend fun clearAndUpsertUserSpecific(userApplications: List<Application>, userId: Int) {
        deleteUserSpecific(userId)
        upsertAll(userApplications)
    }

    @Query("DELETE FROM Application WHERE isPublic = 1")
    suspend fun deletePublic()

    @Query("DELETE FROM Application WHERE userId = :userId")
    suspend fun deleteUserSpecific(userId: Int)

    @Query("DELETE FROM Application")
    suspend fun clearAll()

    @Query("SELECT * FROM Application WHERE id = :appId AND formId = :formId LIMIT 1")
    suspend fun getApplicationByCompositeKey(appId: Int, formId: Int): Application?
}