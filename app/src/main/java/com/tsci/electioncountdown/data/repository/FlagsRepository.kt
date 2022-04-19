package com.tsci.electioncountdown.data.repository

import androidx.annotation.StringRes
import com.tsci.electioncountdown.data.model.CountryItem
import retrofit2.http.GET
import retrofit2.http.Path

interface FlagsRepository {

    suspend fun getCountryById(id: String): CountryItem

    suspend fun getCountryByName(name: String): CountryItem?

    suspend fun getFlagUrlByCountryName(name: String): String

    suspend fun getFlagUrlByCountryId(id: String): String
}