package com.example.quoridor.customView.playerView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.lifecycle.MutableLiveData
import com.example.quoridor.R
import com.example.quoridor.customView.ObservableView
import com.example.quoridor.databinding.CustomViewPvpPlayerInfoBinding
import com.example.quoridor.util.Func

class PvpPlayerInfoView: ObservableView {

    private val binding: CustomViewPvpPlayerInfoBinding by lazy {
        CustomViewPvpPlayerInfoBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_pvp_player_info, this, false)
        )
    }
    val data = MutableLiveData<Player>()

    val profileImageView by lazy {
        binding.playerImageView
    }

    constructor(context: Context): super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        initView()
    }

    private fun initView() {
        addView(binding.root)
        data.observe {
            binding.playerNameTv.text = it.name
            binding.leftTimeTv.text = Func.millToMinSec(it.leftTime)
            binding.leftWallTv.text = it.leftWall.toString()
            binding.playerRatingTv.text = it.rating.toString()
        }
    }
}