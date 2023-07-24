package com.example.quoridor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.quoridor.auth.email.Email
import com.example.quoridor.database.Info
import com.example.quoridor.database.fireDB.FireDB
import com.example.quoridor.databinding.ActivityTestBinding
import com.example.quoridor.ingame.Game1v1
import com.example.quoridor.database.utils.AfterTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class test : ComponentActivity() {
    var context: Context? = null
    var info:Info = Info()
    override fun onCreate(savedInstanceState: Bundle?){
        val binding = ActivityTestBinding.inflate(layoutInflater)
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        context = this

        binding.btnSubmit.setOnClickListener {
            val name: String = binding.ETName.text.toString()
            val email = binding.ETEmail.text.toString()
            val password = binding.ETPw.text.toString()
            if (email == "" || password == "" || name == "") {
                Toast.makeText(context, "빈 칸은 안돼요~", Toast.LENGTH_SHORT).show()
            }
            else{
                Email().signUp(auth, email, password, object : AfterTask {
                    override fun ifSuccess(result: Any?){
                        Email().updateProfile(auth.currentUser, name, object: AfterTask {
                            override fun ifSuccess(result: Any?){
                                info.tier ="hello"
                                FireDB().insert(db,info,object: AfterTask {
                                    override fun ifSuccess(result: Any?) {
                                        finish()
                                    }

                                    override fun ifFail(result: Any?) {
                                        Toast.makeText(context,"실패!!ㅜㅜ",Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }

                            override fun ifFail(result: Any?){
                                Toast.makeText(context,"프로필 등록 실패...", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                    override fun ifFail(result:Any?){
                        Toast.makeText(context, "회원가입 실패...",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }
}
