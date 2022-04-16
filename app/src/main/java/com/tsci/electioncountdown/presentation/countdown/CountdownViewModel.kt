
package com.tsci.electioncountdown.presentation.countdown

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tsci.electioncountdown.common.Constants
import com.tsci.electioncountdown.data.model.CountryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flowOf
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@SuppressWarnings("SimpleDateFormat")
@HiltViewModel
class CountdownViewModel @Inject constructor(
    context: Context,
    private val database: List<CountryItem>
) : ViewModel() {

    companion object {
        val COUNTRY_NAME: String = "country_name"
    }

    val countryItem: CountryItem = CountryItem(
        country = "Turkey",
        countryCode = "tr",
        id = "1",
        lastElection = "24-07-2018 00:00:00",
        nextElection = "25-07-2023 00:00:00"
    )

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
        return database.find { countryItem -> countryItem.country == countryName }
    }
}

private fun SharedPreferences.getString(countryName: String): String? {
    return getString(countryName)
}
