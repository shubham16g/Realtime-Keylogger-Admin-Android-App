package com.shubhamgupta16.realtimekeyloggeradmin.helpers

import com.google.firebase.database.*

enum class EventCase {
    ADDED, CHANGED, REMOVED
}

open class DatabaseListener<T>(
    private val path: String,
    private val rtDB: DatabaseReference,
    private val snapshotToModel: (DataSnapshot) -> T?
) {

    var childListener: ((case: EventCase, model: T, prevKey: String?) -> Unit)? = null


    fun singeEventListener(listener: (list: List<T>) -> Unit) {
        rtDB.child(path).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listener(snapshot.children.mapNotNull { snapshotToModel(it) })
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    fun removeChild(key:String){
        rtDB.child(path).child(key).removeValue()
    }

    fun enableEventListener() {
        rtDB.child(path).addChildEventListener(childEventListener)
    }

    fun disableEventListener() {
        rtDB.child(path).removeEventListener(childEventListener)
    }

    fun setChildValue(key: String, value: Any) {
        rtDB.child(path).child(key).setValue(value)
    }

    fun updateChildrenValue(key: String, value: Map<String, Any>) {
        rtDB.child(path).child(key).updateChildren(value)
    }

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            childListener?.let {
                val model = snapshotToModel(snapshot)
                model?.let { it1 -> it(EventCase.ADDED, it1, previousChildName) }
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            childListener?.let {
                val model = snapshotToModel(snapshot)
                model?.let { it1 -> it(EventCase.CHANGED, it1, previousChildName) }
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            childListener?.let {
                val model = snapshotToModel(snapshot)
                model?.let { it1 -> it(EventCase.REMOVED, it1, null) }
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}
    }
}

