package com.example.quoridor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quoridor.adapter.HistoryRecyclerViewAdapter
import com.example.quoridor.databinding.ActivityHistoryBinding
import com.example.quoridor.history.HistoryViewModel

class HistoryActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityHistoryBinding.inflate(layoutInflater)
    }
    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.historyRecyclerView.apply {
            adapter = HistoryRecyclerViewAdapter(viewModel.getList()) {
                viewModel.loadMoreHistories()
            }
            layoutManager = LinearLayoutManager(
                this@HistoryActivity,
                LinearLayoutManager.VERTICAL,
                false)
        }

        binding.historyRecyclerView.adapter!!.also { that ->
            viewModel.historyList.observeInsert {
                that.notifyItemInserted(it)
            }
            viewModel.historyList.observeRemove {
                that.notifyItemRemoved(it)
            }
            viewModel.historyList.observeChange {
                that.notifyItemChanged(it)
            }
        }

    }
}