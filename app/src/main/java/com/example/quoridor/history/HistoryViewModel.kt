package com.example.quoridor.history

import androidx.lifecycle.ViewModel
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.communication.retrofit.Service
import com.example.quoridor.communication.retrofit.util.SuccessfulHttpResult

class HistoryViewModel: ViewModel() {

    private val service = Service()

    private val TAG = "Dirtfy Test - HistoryViewModel"

    val historyList by lazy {
        MutableLiveListData<HttpDTO.HistoriesResponse>()
    }
    
    fun getList(): MutableList<HttpDTO.HistoriesResponse> {
        return historyList.value!!
    }

    fun loadMoreHistories() {
        service.histories(
            historyList.value?.last()?.gameId?: 0,
            SuccessfulHttpResult {
                historyList.addAll(it)
            })
    }

}