package com.kapanen.cariadtesttask.model

import com.google.gson.annotations.SerializedName

data class AddressInfo(
    @SerializedName("Title") val title: String,
    @SerializedName("AddressLine1") val addressLine1: String,
    @SerializedName("AddressLine2") val addressLine2: String,
    @SerializedName("Town") val town: String,
    @SerializedName("StateOrProvince") val stateOrProvince: String,
    @SerializedName("Postcode") val postcode: String,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Longitude") val longitude: Double,
    @SerializedName("Country") val country: Country
)
