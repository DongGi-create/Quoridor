package com.example.quoridor.ranking

import androidx.lifecycle.ViewModel
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import com.example.quoridor.history.MutableLiveListData
import com.example.quoridor.login.UserManager

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

    fun loadMoreRanking(after: (List<HttpDTO.Response.RankingUser>) -> Unit) {
        httpService.underRanking(
            if((rankingList.value?.size ?: 0) == 0) defaultUid
            else rankingList.value?.last()?.uid?: defaultUid,
            SuccessfulHttpResult {
                rankingList.addAll(it.rankingUserList.subList(1, it.rankingUserList.size))
                after(it.rankingUserList.subList(1, it.rankingUserList.size))
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