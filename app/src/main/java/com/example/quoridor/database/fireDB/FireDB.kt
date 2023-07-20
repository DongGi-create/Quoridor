package com.example.quoridor.database.fireDB

import android.util.Log
import com.example.quoridor.database.Info
import com.example.quoridor.utils.AfterTask
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class FireDB {
    /*fun insert(db: FirebaseFirestore, Info: Info, afterTasks: List<AfterTask?>) {
        // 함수 내부 로직
    }*/
    fun insert(db: FirebaseFirestore, info: Info, afterTasks: List<AfterTask>) {
        // 함수 내부 로직
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            for (at in afterTasks) {
                at.ifFail(null)
            }
            return
        }
        db.collection(COL_USERS).document(user.uid)
            .set(info)
            .addOnSuccessListener { void: Void ->
                Log.d(TAG, "insert:success")
                for (at in afterTasks) {
                    at.ifSuccess(void)
                }
            }
            .addOnFailureListener{ e: Exception?->
                Log.d(TAG, "insert:failure", e)
                for (at in afterTasks){
                    at.ifFail(e)
                }
            }
    }

    fun delete(db: FirebaseFirestore, info: Info, vararg afterTasks: AfterTask) { //varang : 가변인자
        // 함수 내부 로직
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            for (at in afterTasks) {
                at.ifFail(null)
            }
            return
        }
        db.collection(COL_USERS).document(user.uid)
            .delete().addOnSuccessListener { aVoid: Void? ->
                Log.d(TAG, "delete:success")
                for (at in afterTasks) {
                    at.ifSuccess(aVoid)
                }
            }
            .addOnFailureListener { e: Exception? ->
                Log.w(TAG, "delete:failure", e)
                for (at in afterTasks) {
                    at.ifFail(e)
                }
            }
    }

    /*fun loadAllScheduleDuring(
        db: FirebaseFirestore, begin: Long?, end: Long?,
        schedules: ArrayList<Schedule?>, vararg afterTasks: AfterTask?
    ) {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            for (at in afterTasks) {
                at.ifFail(null)
            }
            return
        }
        db.collection(FireStore.COL_USERS).document(FireStore.DOC_SCHEDULES)
            .collection(user.uid)
            .whereLessThan("timeMillis", end!!).whereGreaterThan("timeMillis", begin!!)
            .get().addOnCompleteListener { task: Task<QuerySnapshot> ->
                if (task.isSuccessful) {
                    for (document in task.result) {
                        Log.d(
                            FireStore.TAG,
                            document.id + " => " + document.data
                        )
                        val ts: Schedule = document.data as Schedule
                        ts.serverId = document.id
                        schedules.add(ts)
                    }
                    Log.d(FireStore.TAG, "loadAllScheduleDuring:success")
                    for (at in afterTasks) {
                        at.ifSuccess(task)
                    }
                } else {
                    Log.d(
                        FireStore.TAG,
                        "loadAllScheduleDuring:failure",
                        task.exception
                    )
                    for (at in afterTasks) {
                        at.ifFail(task)
                    }
                }
            }
    }*/
    companion object {
        const val TAG = "db.firestore : minseok"
        const val COL_USERS = "Users"
    }
}