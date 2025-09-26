package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.applicant.Usertype

@Dao
interface ApplicantDao {

    @Query("Select jwt from applicant where userid =:userid")
    fun getJwt(userid: Int) : String?


    @Query("Select userid from applicant")
    fun getUserId() : List<Int>
    @Insert
    suspend fun saveJwt(jwt : List<String>)

    @Insert(onConflict = OnConflictStrategy.IGNORE) //we want to be able to create users with the same username and password
    suspend fun saveApplicant(applicant: Applicant)




}