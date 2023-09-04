package com.example.quoridor.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quoridor.R
import com.example.quoridor.communication.retrofit.HttpDTO
import com.example.quoridor.databinding.ItemRankingRecyclerViewBinding
import com.example.quoridor.databinding.ItemRecyclerViewLoadMoreFooterBinding

class RankingRecyclerViewAdapter(
    val itemList: MutableList<HttpDTO.Response.RankingUser>,
    val footerClickListener: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRankingRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HttpDTO.Response.RankingUser) {
            val name = data.name
            val rating = data.score

            binding.nameTextView.text = name
            binding.ratingTextView.text = rating.toString()
            val ranking = (adapterPosition+1).toString()
            binding.rankingTextView.text = ranking

            binding.ratingImageView.setImageResource(
                when (rating) {
                    in 0 until 1000 -> R.drawable.baseline_workspace_premium_24_purple
                    in 1000 until 1200 -> R.drawable.baseline_workspace_premium_24_yellow
                    in 1200 until 1500 -> R.drawable.baseline_workspace_premium_24_blue
                    else -> R.drawable.baseline_workspace_premium_24_green
                }
            )
        }
    }

    inner class Footer(val binding: ItemRecyclerViewLoadMoreFooterBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemList.size) R.layout.item_recycler_view_load__more_footer
        else R.layout.item_ranking_recycler_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)

        return if (viewType == R.layout.item_ranking_recycler_view){
            ViewHolder(ItemRankingRecyclerViewBinding.bind(view))
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
            (holder as RankingRecyclerViewAdapter.ViewHolder).apply {
                val item = itemList[position]
                bind(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.count() + 1
    }
}