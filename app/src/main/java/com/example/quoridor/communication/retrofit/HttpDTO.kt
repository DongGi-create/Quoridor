package com.example.quoridor.communication.retrofit

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

object HttpDTO {
    object Request {
        data class Login(
            @SerializedName("loginId")
            val loginId: String,
            @SerializedName("password")
            val password: String
        )

        data class Signup(
            @SerializedName("loginId")
            val loginId: String,
            @SerializedName("password")
            val password: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("name")
            val name: String,
        )

        data class UserUpdate(
            @SerializedName("password")
            val password: String?,
            @SerializedName("email")
            val email: String?,
            @SerializedName("name")
            val name: String?
        )

        data class Match(
            @SerializedName("gameType")
            val gameType: Int
        )
    }

    object Response {
        data class User(
            @SerializedName("loginId")
            val loginId: String,
            @SerializedName("password")
            val password: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("name")
            val name: String,
            @SerializedName("score")
            val score: Int,
            @SerializedName("uid")
            val uid: Long,
            @SerializedName("totalGames")
            val totalGames: Int,
            @SerializedName("winGames")
            val winGames: Int
        )

        data class Match(
            @SerializedName("gameId")
            val gameId: String?,
            @SerializedName("turn")
            val turn: Int?,
            @SerializedName("opponentName")
            val opponentName: String?,
            @SerializedName("opponentScore")
            val opponentScore: Int?
        )

        data class CompHistory(
            @SerializedName("gameId")
            val gameId: Long,
            @SerializedName("win")
            val win: Boolean,
            @SerializedName("opponentName")
            val opponentName: String,
            @SerializedName("opponentScore")
            val opponentScore: Int,
            @SerializedName("opponentProfileImage")
            val opponentProfileImage: String
        )

        data class DetailHistory(
            @SerializedName("gameId")
            val gameId: Long,
            @SerializedName("uid0")
            val uid0: Long,
            @SerializedName("uid1")
            val uid1: Long,
            @SerializedName("score0")
            val score0: Int,
            @SerializedName("score1")
            val score1: Int,
            @SerializedName("stamp")
            val stamp: Timestamp,
            @SerializedName("moves")
            val moves: String,
            @SerializedName("winnerId")
            val winnerId: Long
        )

        data class RankingUser(
            @SerializedName("score")
            val score: Int,
            @SerializedName("uid")
            val uid: Long,
            @SerializedName("name")
            val name: String
        )

        data class Rank(
            @SerializedName("firstElementRank")
            val firstElementRank: String,
            @SerializedName("rankingUserList")
            val rankingUserList: List<RankingUser>
        )
    }
}
