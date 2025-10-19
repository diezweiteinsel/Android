package de.cau.inf.se.sopro.persistence.dao

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.cau.inf.se.sopro.persistence.LocDatabase
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): LocDatabase {
        return Room.databaseBuilder(
            context,
            LocDatabase::class.java, "database"
        )
            .fallbackToDestructiveMigration(false)
            .build()
    }

    @Provides
    fun provideApplicantDao(db: LocDatabase): ApplicantDao = db.applicantDao()

    @Provides
    fun provideApplicationDao(db: LocDatabase): ApplicationDao = db.applicationDao()

    @Provides
    fun provideFormDao(db: LocDatabase): FormDao = db.formDao()

    @Provides
    fun provideBlockDao(db: LocDatabase): BlockDao = db.blockDao()
}