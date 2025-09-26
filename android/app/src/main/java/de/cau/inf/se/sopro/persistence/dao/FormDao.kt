package de.cau.inf.se.sopro.persistence.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import de.cau.inf.se.sopro.model.application.Form
import kotlinx.coroutines.flow.Flow

@Dao
interface FormDao {
    @Upsert
    suspend fun saveForm(form : Form) //

    @Query("Select * From Form") //get forms locally from the db
    suspend fun getForms() : Flow<List<Form>>
}