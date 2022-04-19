package com.tsci.electioncountdown.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.tsci.electioncountdown.common.Constants
import com.tsci.electioncountdown.data.model.CountryItem
import com.tsci.electioncountdown.data.repository.FlagsRepository
import com.tsci.electioncountdown.data.repository.FlagsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {


    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun provideDatabase(context: Context): List<CountryItem> {

        val jsonString = context.assets.open(Constants.JSON_FILE).bufferedReader().use {
            it.readText()
        }
        return GsonBuilder()
            .create()
            .fromJson(jsonString, Array<CountryItem>::class.java).toList()
    }

    @Provides
    @Singleton
    fun provideFlagsRepository(database: List<CountryItem>): FlagsRepository {
        return FlagsRepositoryImpl(database = database)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(appContext)
    }

}
