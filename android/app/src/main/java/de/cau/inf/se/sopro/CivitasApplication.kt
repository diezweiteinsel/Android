package de.cau.inf.se.sopro

import android.app.Application
import de.cau.inf.se.sopro.di.AppContainer
import de.cau.inf.se.sopro.di.DefaultAppContainer

class CivitasApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
