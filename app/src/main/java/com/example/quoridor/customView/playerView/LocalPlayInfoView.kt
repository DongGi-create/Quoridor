package com.example.quoridor.customView.playerView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.example.quoridor.R
import com.example.quoridor.customView.ObservableView
import com.example.quoridor.databinding.CustomViewLocalPlayInfoBinding
import com.example.quoridor.util.Func

class LocalPlayInfoView: ObservableView {

    private val binding: CustomViewLocalPlayInfoBinding by lazy {
        CustomViewLocalPlayInfoBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_local_play_info, this, false)
        )
    }
    val data = MutableLiveData<Player>()

    constructor(context: Context): super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        initView()
    }

    private fun initView() {
        addView(binding.root)
        data.observe {
            binding.leftWallTv.text = it.leftWall.toString()
            binding.leftTimeTv.text = Func.millToMinSec(it.leftTime)
            binding.tvLayoutOverlay.visibility = if (it.myTurn) View.INVISIBLE else View.VISIBLE
        }
    }
}