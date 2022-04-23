package com.shubhamgupta16.realtimekeyloggeradmin.helpers

import android.content.Intent
import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.shubhamgupta16.realtimekeyloggeradmin.models.DeviceModel
import java.text.SimpleDateFormat
import java.util.*

fun Long.displayDate(): String {
    val sf = SimpleDateFormat("hh:mm a, dd MMM, yyyy", Locale.US)
    return sf.format(this)
}

fun List<DeviceModel>.getPositionByDeviceKey(deviceKey: String?): Int? {
    if (deviceKey == null) return null
    for (i in this.indices) {
        if (this[i].deviceKey == deviceKey)
            return i
    }
    return null
}

fun Intent.getDeviceModel(): DeviceModel? {
    return getSerializableExtra("device") as? DeviceModel
}

fun getDbReference(): DatabaseReference {
    return FirebaseDatabase.getInstance().reference
}

fun DataSnapshot.getDeviceModel(): DeviceModel? {
    if (this.key == null) return null
    if (!this.hasChild("name")) return null
    if (!this.hasChild("status")) return null
    return DeviceModel(
        this.key!!,
        this.child("name").value.toString().trim(),
        (this.child("status").value as Long).toInt(),
        this.child("lastOnline").value as Long
    )
}

fun String?.decode(): String? {
    if (this == null) return null
    return Uri.decode(this)
}