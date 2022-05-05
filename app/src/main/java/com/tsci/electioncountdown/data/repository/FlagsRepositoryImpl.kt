package com.tsci.electioncountdown.data.repository

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.google.gson.JsonObject
import com.tsci.electioncountdown.common.Constants.BASE_URL
import com.tsci.electioncountdown.data.model.CountryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.net.URL
import javax.inject.Inject

private const val TAG = "FlagsRepositoryImpl.kt"
class FlagsRepositoryImpl @Inject constructor(
    private val database: List<CountryItem>
)  : FlagsRepository {

    override suspend fun getCountryByName(name: String): Flow<CountryItem?> {
        database.forEach {
            if(it.country.equals(name)) return flowOf(it)
        }
        return flowOf(null)
    }

    override fun getCountryByCode(code: String): CountryItem? {
        var item: CountryItem? = null
        database.forEach {
            if (it.countryCode.uppercase().equals(code))
                item = it
        }
        return item
    }
}