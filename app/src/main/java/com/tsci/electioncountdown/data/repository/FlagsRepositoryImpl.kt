package com.tsci.electioncountdown.data.repository

import android.content.res.Resources
import com.google.gson.JsonObject
import com.tsci.electioncountdown.common.Constants.BASE_URL
import com.tsci.electioncountdown.data.model.CountryItem
import java.net.URL
import javax.inject.Inject

class FlagsRepositoryImpl @Inject constructor(
    private val database: List<CountryItem>
)  : FlagsRepository {

    override suspend fun getCountryById(id: String): CountryItem {
        database.forEach {
            if(it.id.equals(id)) return it
        }
        throw Resources.NotFoundException("Country Not Found!")
    }

    override suspend fun getCountryByName(name: String): CountryItem? {
        database.forEach {
            if(it.country.equals(name)) return it
        }
        return null
    }

    override suspend fun getFlagUrlByCountryName(name: String): String {
        database.forEach {
            if(it.country.equals(name)) return BASE_URL + name
        }
        throw Resources.NotFoundException("Country Not Found!")
    }

    override suspend fun getFlagUrlByCountryId(id: String): String {
        database.forEach {
            if(it.id == id) return BASE_URL + it.country
        }
        throw Resources.NotFoundException("Country Not Found!")
    }
}