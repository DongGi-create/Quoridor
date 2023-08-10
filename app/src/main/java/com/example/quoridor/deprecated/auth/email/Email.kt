package com.example.quoridor.deprecated.auth.email

import android.util.Log
import com.example.quoridor.deprecated.database.utils.AfterTask
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlin.math.E

class Email {
    /**
     * `afterTask(AuthResult)`<br></br>
     * `task` is `@NonNull`
     */
    fun signUp(mAuth: FirebaseAuth, email: String?, password: String?, afterTask: AfterTask){
        mAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener{ task: Task<AuthResult?> ->
                if(task.isSuccessful){
                    Log.d(TAG, "signUp: success")
                    afterTask.ifSuccess(task)
                } else{
                    Log.w(TAG, "signUp:failure", task.exception)
                    afterTask.ifFail(task)
                }
            }
    }

    fun signIn(mAuth: FirebaseAuth, email: String?, password: String?, afterTask: AfterTask){
        mAuth.signInWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener{task: Task<AuthResult?> ->
                if(task.isSuccessful){
                    Log.d(TAG, "signIn:success")
                    val user = mAuth.currentUser
                    afterTask.ifSuccess(task)
                }else{
                    Log.w(TAG, "signIn:failure", task.exception)
                    afterTask.ifFail(task)
                }
            }
    }

    fun delete(user: FirebaseUser, afterTask: AfterTask){
        user.delete()
            .addOnCompleteListener { task: Task<Void?> ->
                if (task.isSuccessful) {
                    Log.d(TAG, "user delete:success")
                    afterTask.ifSuccess(task)
                } else {
                    Log.d(TAG, "user delete:failure", task.exception)
                    afterTask.ifFail(task)
                }
            }
    }

    fun updateProfile(user: FirebaseUser?, name: String, afterTask: AfterTask){
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()
        if(user != null) {
            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        afterTask.ifSuccess(task)
                    } else {
                        afterTask.ifFail(task)
                    }
                }
        }
    }

    fun getName(user: FirebaseUser, afterTask: AfterTask?): String?{
        return user.displayName
    }

    companion object{
        const val TAG = "auth.email : minseok"
    }
}