package com.bigmeco.testdfm

import android.app.Application
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SplitCompat.install(this)

        startKoin {
            androidContext(this@MainApplication)

            // Список модулей
            modules(listOf(appModule))
        }
    }
}

val appModule = module {

}