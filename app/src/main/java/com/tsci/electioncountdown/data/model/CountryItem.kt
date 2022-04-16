package com.tsci.electioncountdown.data.model

import com.google.gson.annotations.SerializedName

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