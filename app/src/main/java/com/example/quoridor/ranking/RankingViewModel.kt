package com.example.quoridor.ranking

import com.example.quoridor.history.MutableLiveListData

import androidx.lifecycle.ViewModel
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.deprecated.domain.User
import com.example.quoridor.login.UserManager
import java.util.Calendar

class RankingViewModel: ViewModel() {

    private val httpService = HttpService()
    private var defaultUid = -1L

    private val TAG = "Dirtfy Test - RankingViewModel"

    val rankingList by lazy {
        MutableLiveListData<HttpDTO.Response.RankingUser>()
    }

    fun getList(): MutableList<HttpDTO.Response.RankingUser> {
        return rankingList.value!!
    }

    fun loadFirstRanking(after: () -> Unit = {}) {
        httpService.underRanking(
            defaultUid,
            SuccessfulHttpResult {
                rankingList.addAll(it.rankingUserList)
                after()
            }
        )
    }

    fun loadMoreRanking() {
        httpService.underRanking(
            if((rankingList.value?.size ?: 0) == 0) defaultUid
            else rankingList.value?.last()?.uid?: defaultUid,
            SuccessfulHttpResult {
                rankingList.addAll(it.rankingUserList.subList(1, it.rankingUserList.size))
            })
    }

    fun loadMyRanking(after: (Int) -> Unit) {
        httpService.underRanking(
            UserManager.umuid?:-1,
            SuccessfulHttpResult {
                after(it.firstElementRank.split("/")[0].toInt())
            }
        )
    }

}