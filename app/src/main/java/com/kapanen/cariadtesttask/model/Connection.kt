package com.kapanen.cariadtesttask.model

import com.google.gson.annotations.SerializedName

data class Connection(
    @SerializedName("ConnectionType") val connectionType: ConnectionType
)
