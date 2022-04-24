package com.tsci.electioncountdown.data.repository

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import com.tsci.electioncountdown.data.model.CountryItem
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path

interface FlagsRepository {

    suspend fun getCountryById(id: String): CountryItem

    fun getCountryByName(name: String): Flow<CountryItem?>

    fun getCountryByCode(code: String): Flow<CountryItem?>

    suspend fun getFlagUrlByCountryName(name: String): String

    suspend fun getFlagUrlByCountryId(id: String): String
}