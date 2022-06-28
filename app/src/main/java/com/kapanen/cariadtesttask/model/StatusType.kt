package com.kapanen.cariadtesttask.model

import com.google.gson.annotations.SerializedName

data class StatusType(
    @SerializedName("IsOperational") val IsOperational: Boolean,
    @SerializedName("IsUserSelectable") val IsUserSelectable: Boolean
)
