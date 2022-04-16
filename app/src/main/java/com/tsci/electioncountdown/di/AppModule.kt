package com.tsci.electioncountdown.di

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tsci.electioncountdown.common.Constants
import com.tsci.electioncountdown.data.model.CountryItem
import com.tsci.electioncountdown.data.repository.FlagsRepository
import com.tsci.electioncountdown.data.repository.FlagsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
        val countries: List<CountryItem> =
            GsonBuilder()
                .create()
                .fromJson(jsonString, Array<CountryItem>::class.java).toList()
        return countries
    }

    @Provides
    @Singleton
    fun provideFlagsRepository(database: List<CountryItem>): FlagsRepository {
        return FlagsRepositoryImpl(database = database)
    }
}