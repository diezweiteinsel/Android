package de.cau.inf.se.sopro.persistence

import androidx.room.Database
import de.cau.inf.se.sopro.model.applicant.Applicant
import de.cau.inf.se.sopro.persistence.dao.ApplicantDao
import de.cau.inf.se.sopro.persistence.dao.ApplicationDao
import de.cau.inf.se.sopro.persistence.dao.FormDao

@Database(entities = [ApplicantDao::class, ApplicationDao::class, FormDao::class], version = 1)
abstract class Database {

    abstract val applicationDao: ApplicationDao
    abstract val applicantDao: ApplicantDao
    abstract val formDao : FormDao

    //TODO: Dependency Injection
}