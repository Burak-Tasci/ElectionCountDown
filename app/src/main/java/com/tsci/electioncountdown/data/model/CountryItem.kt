package com.tsci.electioncountdown.data.model

import com.google.gson.annotations.SerializedName
import com.tsci.electioncountdown.common.Constants

data class CountryItem(
    @SerializedName("Country")
    val country: String,
    @SerializedName("Country Code")
    val countryCode: String,
    @SerializedName("ID")
    val id: String,
    @SerializedName("Last Election")
    val lastElection: String,
    @SerializedName("Next Election")
    val nextElection: String
)
internal fun CountryItem.getFlagImageUrl(): String {
    return Constants.BASE_URL + this.countryCode
}