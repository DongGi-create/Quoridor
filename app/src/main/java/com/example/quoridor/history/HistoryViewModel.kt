package com.example.quoridor.history

import androidx.lifecycle.ViewModel
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.HttpService
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult
import java.util.Calendar

class HistoryViewModel: ViewModel() {

    private val httpService = HttpService()
    private var defaultGameId = 0L

    private val TAG = "Dirtfy Test - HistoryViewModel"

    val historyList by lazy {
        MutableLiveListData<HttpDTO.Response.CompHistory>()
    }

    init {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 1)
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)+1
        val day = cal.get(Calendar.DATE)
        defaultGameId = ((year*10000 + month*100 + day)*(10e+11)).toLong()
    }

    fun getList(): MutableList<HttpDTO.Response.CompHistory> {
        return historyList.value!!
    }

    fun loadRecentHistories(after: () -> Unit = {}) {
        httpService.histories(
            defaultGameId,
            SuccessfulHttpResult {
                historyList.addAll(it)
                after()
            }
        )
    }

    fun loadMoreHistories(after: (List<HttpDTO.Response.CompHistory>) -> Unit = {}) {
        httpService.histories(
             if((historyList.value?.size ?: 0) == 0) defaultGameId
             else historyList.value?.last()?.gameId?: defaultGameId,
            SuccessfulHttpResult {
                historyList.addAll(it)
                after(it)
            })
    }

}