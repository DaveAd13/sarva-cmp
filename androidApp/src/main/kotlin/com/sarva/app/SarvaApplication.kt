package com.sarva.app

import android.app.Application
import android.os.StrictMode
import com.sarva.app.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class SarvaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@SarvaApplication)
        }

//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(
//                StrictMode.ThreadPolicy.Builder()
//                    .penaltyDialog()
//                    .detectAll()
//                    .penaltyLog()
//                    .build()
//            )
//        }
    }
}