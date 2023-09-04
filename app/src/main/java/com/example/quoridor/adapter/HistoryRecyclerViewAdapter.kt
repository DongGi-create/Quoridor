package com.example.quoridor.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quoridor.R
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.databinding.ItemHistoryRecyclerViewBinding
import com.example.quoridor.databinding.ItemRecyclerViewLoadMoreFooterBinding

class HistoryRecyclerViewAdapter(
    val itemList: MutableList<HttpDTO.Response.CompHistory>,
    val itemClickListener: (HttpDTO.Response.CompHistory) -> Unit,
    val footerClickListener: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val binding: ItemHistoryRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HttpDTO.Response.CompHistory) {
            binding.opponentName.text = data.opponentName
            binding.opponentRating.text = data.opponentScore.toString()
            binding.resultIamgeView.setImageResource(
                when(data.win) {
                    true -> R.drawable.baseline_lens_24_green
                    false -> R.drawable.baseline_lens_24_red
                }
            )
        }
    }

    inner class Footer(val binding: ItemRecyclerViewLoadMoreFooterBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemList.size) R.layout.item_recycler_view_load__more_footer
                else R.layout.item_history_recycler_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return if (viewType == R.layout.item_history_recycler_view){
            ViewHolder(ItemHistoryRecyclerViewBinding.bind(view))
        } else {
            val holder = Footer(ItemRecyclerViewLoadMoreFooterBinding.bind(view))
            holder.binding.loadMoreButton.setOnClickListener {
                footerClickListener()
            }
            holder
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder !is Footer) {
            (holder as ViewHolder).apply {
                val item = itemList[position]
                bind(item)
                binding.root.setOnClickListener {
                    itemClickListener(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count() + 1
    }

}