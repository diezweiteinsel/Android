package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import de.cau.inf.se.sopro.model.applicant.Applicant

@Dao
interface ApplicantDao {

    @Query("Select jwt from applicant where userid = :userId")
    fun getJwt(userId: Int) : String?


    @Query("Select userid from applicant")
    fun getUserId() : Int

    @Upsert
    suspend fun saveJwt(applicant: Applicant)

    @Insert(onConflict = OnConflictStrategy.IGNORE) //we want to be able to create users with the same username and password
    suspend fun saveApplicant(applicant: Applicant)

}
