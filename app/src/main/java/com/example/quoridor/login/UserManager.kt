package com.example.quoridor.login

import com.example.quoridor.retrofit.DTO

object UserManager {
    var umid: String? = ""//로그인 안할때는 빈문자열
        private set
    var umpw: String? = null
        private set
    var umemail: String? = null
        private set
    var umname: String? = null
        private set
    var umscore: String? = null
        private set

    @Volatile
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
    }

    fun setUser(user: DTO.SignUpResponse){
        umid = user.loginId
        umpw = user.password
        umemail = user.email
        umname = user.name
        umscore = user.score
    }

    fun setId(i: String){
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
    }
}
