package com.tsci.electioncountdown.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tsci.electioncountdown.data.model.CountryItem
import com.tsci.electioncountdown.data.model.getFlagImageUrl
import com.tsci.electioncountdown.data.repository.FlagsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

private const val TAG = "MainViewModel.kt"

@SuppressWarnings("SimpleDateFormat")
@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Context,
    private val repository: FlagsRepository,
    private val sharedPreferences: SharedPreferences,
    database: List<CountryItem>
) : ViewModel() {

    val countryItem by lazy { MutableLiveData<CountryItem>() }

    companion object {
        const val COUNTRY_NAME: String = "country_name"
    }

    init {
        setCountry()
    }

    private fun setCountry() {
        countryItem.value = getCountryItemByName(
            sharedPreferences.getString(COUNTRY_NAME, null).toString()
        )
        if (countryItem.value == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                countryItem.value =
                    getCountryItemByCode(context.resources.configuration.locales[0].country)
                Log.d(TAG, "setCountry-Code:${context.resources.configuration.locales[0].country}")
                Log.d(TAG, "setCountry-If: ${countryItem.value}")
            } else {
                countryItem.value =
                    getCountryItemByCode(context.resources.configuration.locale.country)
                Log.d(TAG, "setCountry-Else: ${countryItem.value}")
            }
            Log.d(TAG, "setCountry-End: ${countryItem.value}")
            sharedPreferences.edit().putString(COUNTRY_NAME, countryItem.value?.country).apply()
        }
    }

    internal fun countDownTime(): Long {
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        try {
            //Formatting from String to Date
            val date = sdf.parse(countryItem.value?.nextElection.toString())
            return date!!.time - Date().time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return -1L
    }

    fun getProgress(): LiveData<Int> {

        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        return if (countryItem.value != null) {
            val startDate = sdf.parse(countryItem.value!!.lastElection)
            val endDate = sdf.parse(countryItem.value!!.nextElection)

            val diff = endDate!!.time - startDate!!.time
            val currentStateDiff = endDate.time - Date().time

            if (currentStateDiff <= 0)
                flowOf(0).asLiveData()
            else
                flowOf(100 - (currentStateDiff * 100 / diff).toInt()).asLiveData()
        } else {
            flowOf(0).asLiveData()
        }
    }

    private fun getCountryItemByCode(code: String): CountryItem? {

        return repository.getCountryByCode(code)
    }

    internal fun getFlagImageUrl(): String {
        return countryItem.value?.getFlagImageUrl().toString()
    }

    internal fun getCountryItemByName(countryName: String): CountryItem? {

        var countryItem: CountryItem? = null
        runBlocking {
            repository.getCountryByName(countryName).collect {
                countryItem = it
            }
        }
        return countryItem
    }
}