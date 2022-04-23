package com.shubhamgupta16.realtimekeyloggeradmin.models

import com.google.firebase.database.DataSnapshot
import java.io.Serializable

data class EventModel(
    val event: Int, val text: String?, val desc: String?, val nTitle: String?,
    val nText: String?, val nText2: String?, val nPackage: String?
) : Serializable {
    constructor() : this(0, null, null, null, null, null, null)

    var timestamp: Long? = null
    var isSelected = false

    companion object {
        fun fromSnapshot(snapshot: DataSnapshot): EventModel? {
            return snapshot.getValue(EventModel::class.java)
                ?.apply { timestamp = snapshot.key?.toLong() }
//            return snapshot.getValue(EventModel::class.java)?.apply { timestamp = snapshot.child("time").value as Long }
//            return snapshot.getValue(EventModel::class.java)?.apply { timestamp = (snapshot.child("time").value as Long) + 1644956359761L }
        }
    }
}
