package de.cau.inf.se.sopro.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.model.application.Application
import de.cau.inf.se.sopro.model.application.Form
import de.cau.inf.se.sopro.model.application.Block
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import de.cau.inf.se.sopro.persistence.dao.ApplicationDao
import de.cau.inf.se.sopro.persistence.dao.BlockDao
import de.cau.inf.se.sopro.persistence.dao.FormDao


@Database(entities = [
    Application::class,
    Applicant::class,
    Form::class
],
    version = 3)
@TypeConverters(Converters::class)

abstract class LocDatabase : RoomDatabase(){

    abstract fun applicationDao(): ApplicationDao
    abstract fun applicantDao(): ApplicantDao
    abstract fun formDao(): FormDao

    companion object{
        @Volatile
        private var Instance : LocDatabase? = null

        fun getDatabase(context: Context) : LocDatabase{
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LocDatabase::class.java, "database")
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                    .also{Instance = it}
            }
        }
    }
}