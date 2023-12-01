package com.example.newdata.di

import dagger.hilt.android.HiltAndroidApp
import android.app.Application
import android.content.Context
import javax.inject.Inject
import com.example.newdata.common.AppPreferences
import io.reactivex.disposables.CompositeDisposable

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var appPrefs: AppPreferences

    var disposable = CompositeDisposable()
    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        disposable.dispose()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }
}