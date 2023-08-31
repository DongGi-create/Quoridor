package com.example.quoridor.login

import com.example.quoridor.communication.retrofit.HttpDTO

class UserManager {
    companion object{
        var umid: String? = ""//로그인 안할때는 빈문자열
        //private set//객체 내부에서 setter 금지
        var umpw: String? = null
            private set
        var umemail: String? = null
            private set
        var umname: String? = null
            private set
        var umscore: Int? = null
            private set
        var umuid: Long? = null
            private set
        fun setUser(user: HttpDTO.SignUpResponse){
            umid = user.loginId
            umpw = user.password
            umemail = user.email
            umname = user.name
            umscore = user.score
            umuid = user.uid
        }
    }


    /*@Volatile
    private var instance: UserManager? = null

    fun getInstance(): UserManager {
        if (instance == null) {
            synchronized(this) {
                if (instance == null) {
                    instance = UserManager
                }
            }
        }
        return instance!!
    }*/

    /*fun setId(i: String){
        umid = i
    }
    fun setPw(p: String){
        umpw = p
    }
    fun setEmail(e: String){
        umemail = e
    }
    fun setName(n: String){
        umname = n
    }
    fun setScore(s: String){
        umscore = s
    }

    fun getId(): String?{
        return umid
    }
    fun getPw(): String?{
        return umpw
    }
    fun getEmail(): String?{
        return umemail
    }
    fun getName(): String?{
        return umname
    }
    fun getScore(): String?{
        return umscore
    }*/
}
