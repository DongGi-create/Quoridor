package com.example.quoridor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quoridor.R
import com.example.quoridor.databinding.ItemRankingRecyclerViewBinding
import com.example.quoridor.retrofit.DTO

class RankingRecyclerViewAdapter(val itemList: ArrayList<DTO.SignUpResponse>): RecyclerView.Adapter<RankingRecyclerViewAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding by lazy {
            ItemRankingRecyclerViewBinding.bind(itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_ranking_recycler_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = itemList[position]

        val name = data.name
        val rating = data.score

        holder.binding.nameTextView.text = name
        holder.binding.ratingTextView.text = rating
        holder.binding.rankingTextView.text = "${position+1}"

        val ratingInt = rating.toInt()
        holder.binding.ratingImageView.setImageResource(
            when(ratingInt) {
                in 0 until 1000 -> R.drawable.baseline_workspace_premium_24_purple
                in 1000 until 1200 -> R.drawable.baseline_workspace_premium_24_yellow
                in 1200 until 1500 -> R.drawable.baseline_workspace_premium_24_blue
                else -> R.drawable.baseline_workspace_premium_24_green
            }
        )

    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}