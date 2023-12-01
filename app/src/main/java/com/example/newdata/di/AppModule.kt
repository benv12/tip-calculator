package com.example.newdata.di

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.example.newdata.common.AppPreferences
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun providesPreferences(@ApplicationContext context: Context?, gson: Gson?): AppPreferences {
        return AppPreferences(PreferenceManager.getDefaultSharedPreferences(context), gson!!)
    }

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }


    @Provides
    fun provideGson(): Gson {
        return Gson()
    }
}