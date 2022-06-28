package com.kapanen.cariadtesttask.model

import com.google.gson.annotations.SerializedName

data class Poi(
    @SerializedName("AddressInfo") val addressInfo: AddressInfo,
    @SerializedName("Connections") val connections: List<Connection>,
    @SerializedName("StatusType") val statusType: StatusType,
    @SerializedName("NumberOfPoints") val numberOfPoints: Int
)

fun Poi.isActive() = statusType.IsUserSelectable && statusType.IsUserSelectable
