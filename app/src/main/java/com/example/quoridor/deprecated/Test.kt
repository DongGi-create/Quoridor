package com.example.quoridor.deprecated

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.quoridor.deprecated.database.Info

class test : ComponentActivity() {
    var context: Context? = null
    var info: Info = Info()
    override fun onCreate(savedInstanceState: Bundle?){
//        val binding = ActivityTestBinding.inflate(layoutInflater)
//        val auth = FirebaseAuth.getInstance()
//        val db = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
//        setContentView(binding.root)
//        context = this
//
//        binding.btnSubmit.setOnClickListener {
//            val name: String = binding.ETName.text.toString()
//            val email = binding.ETEmail.text.toString()
//            val password = binding.ETPw.text.toString()
//            if (email == "" || password == "" || name == "") {
//                Toast.makeText(context, "빈 칸은 안돼요~", Toast.LENGTH_SHORT).show()
//            }
//            else{
//                Email().signUp(auth, email, password, object : AfterTask {
//                    override fun ifSuccess(result: Any?){
//                        Email().updateProfile(auth.currentUser, name, object: AfterTask {
//                            override fun ifSuccess(result: Any?){
//                                info.tier ="hello"
//                                FireDB().insert(db,info,object: AfterTask {
//                                    override fun ifSuccess(result: Any?) {
//                                        finish()
//                                    }
//
//                                    override fun ifFail(result: Any?) {
//                                        Toast.makeText(context,"실패!!ㅜㅜ",Toast.LENGTH_SHORT).show()
//                                    }
//                                })
//                            }
//
//                            override fun ifFail(result: Any?){
//                                Toast.makeText(context,"프로필 등록 실패...", Toast.LENGTH_SHORT).show()
//                            }
//                        })
//                    }
//                    override fun ifFail(result:Any?){
//                        Toast.makeText(context, "회원가입 실패...",Toast.LENGTH_SHORT).show()
//                    }
//                })
//            }
//        }
    }
}
