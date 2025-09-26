package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.cau.inf.se.sopro.model.applicant.Usertype

@Dao
interface ApplicantDao {

    @Query("Select jwt from applicant where userId =:userId")
    suspend fun getJwt(userId: Int) : String

    @Insert
    suspend fun saveJwt(jwt : String)

    @Insert(onConflict = OnConflictStrategy.IGNORE) //we want to be able to create users with the same username and password
    suspend fun createApplicant(username: String,password: String, usertype: Usertype)




}