package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import de.cau.inf.se.sopro.model.application.Form
import kotlinx.coroutines.flow.Flow

@Dao
interface FormDao {
    @Insert
    suspend fun saveForm(form : Form) //save forms locally to the db

    @Query("Select * From form") //get forms locally from the db
    fun getForms() : Flow<List<Form>>

    @Query("Select * From form Where name = :name")
    suspend fun getFormByName(name : String) : Form?


}