package com.kapanen.cariadtesttask.model

import com.google.gson.annotations.SerializedName

data class AddressInfo(
    @SerializedName("Title") val title: String,
    @SerializedName("AddressLine1") val addressLine1: String? = null,
    @SerializedName("AddressLine2") val addressLine2: String? = null,
    @SerializedName("Town") val town: String? = null,
    @SerializedName("StateOrProvince") val stateOrProvince: String? = null,
    @SerializedName("Postcode") val postcode: String? = null,
    @SerializedName("Latitude") val latitude: Double? = null,
    @SerializedName("Longitude") val longitude: Double? = null,
    @SerializedName("Country") val country: Country? = null
)
