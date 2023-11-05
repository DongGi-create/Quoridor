package com.example.quoridor

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        viewModel.loadRecentHistories {
            binding.historyRecyclerView.apply {
                adapter = HistoryRecyclerViewAdapter(
                    viewModel.getList(),
                    {
                        val intent = Intent(this@HistoryActivity, HistoryDetailActivity::class.java)
                        intent.putExtra("gameId", it.gameId)
                        startActivity(intent)
                    },
                    {
                        viewModel.loadMoreHistories {
                            if (it.isEmpty()) {
                                Toast.makeText(this@HistoryActivity, "loaded all", Toast.LENGTH_SHORT).show()
                            }
                        }
//                viewModel.historyList.add(HttpDTO.HistoriesResponse(0, false, "didwoahqkqhajdcjddl", 2000))
                    }
                )
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
}