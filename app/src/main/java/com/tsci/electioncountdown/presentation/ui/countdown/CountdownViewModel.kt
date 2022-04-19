package com.tsci.electioncountdown.presentation.ui.countdown

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tsci.electioncountdown.common.Constants
import com.tsci.electioncountdown.data.model.CountryItem
import com.tsci.electioncountdown.data.repository.FlagsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG = "CountdownViewModel.kt"

@SuppressWarnings("SimpleDateFormat")
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class CountdownViewModel @Inject constructor(
    private val context: Context,
    private val repository: FlagsRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {


    companion object {
        const val COUNTRY_NAME: String = "country_name"
    }

    val countryItem: CountryItem = CountryItem(
        country = "Turkey",
        countryCode = "tr",
        id = "1",
        lastElection = "24-07-2018 00:00:00",
        nextElection = "25-07-2023 00:00:00"
    )


    internal fun getCountry(): CountryItem? {

        // FIXME: RESPONSE IS NULL 
        sharedPreferences.edit().clear().apply()

        var country: CountryItem? = getCountryItemByName(
            sharedPreferences.getString(COUNTRY_NAME, null).toString()
        )
        if (country == null) {

            val client = OkHttpClient()
            val request = Request.Builder()
                .url("http://ip-api.com/json")
                .build()

            // Coroutines not supported directly, use the basic Callback way:
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        if (!response.isSuccessful) throw IOException("Unexpected code $response")

                        for ((name, value) in response.headers) {
                            Log.d(TAG, "onResponse: \"$name: $value\"")
                        }

                        val jsonObject = JSONObject(response.body!!.string())
                        val countryName = jsonObject.get("country").toString()
                        Log.d(TAG, "onResponse: jsonObject-country: $countryName")
                        country = getCountryItemByName(countryName)
                        Log.d(TAG, "onResponse: countryItem: ${country.toString()}")
                        sharedPreferences.edit().putString(COUNTRY_NAME, countryName).commit()
                    }
                }
            })
        }

        Log.d(TAG, "getCountry: country-response: ${country.toString()}")
        return country
    }

    internal fun countDownTime(): Long {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        try {
            //Formatting from String to Date
            val date = sdf.parse(countryItem.nextElection.toString())
            return date!!.time - Date().time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return -1L
    }

    internal fun getProgress(): LiveData<Int> {

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        val startDate = sdf.parse(countryItem.lastElection)
        val endDate = sdf.parse(countryItem.nextElection)

        val diff = endDate!!.time - startDate!!.time
        val currentStateDiff = endDate.time - Date().time

        return if (currentStateDiff <= 0)
            flowOf(0).asLiveData()
        else
            flowOf(100 - (currentStateDiff * 100 / diff).toInt()).asLiveData()

    }

    internal fun getFlagImageUrl(): String {
        return Constants.BASE_URL + "/" + countryItem.countryCode
    }

    private fun getCountryItemByName(countryName: String): CountryItem? {

        var countryItem: CountryItem? = null
        viewModelScope.launch {
            countryItem = repository.getCountryByName(countryName)
        }
        return countryItem

    }


}


