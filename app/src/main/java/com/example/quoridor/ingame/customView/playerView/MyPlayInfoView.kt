package com.example.quoridor.ingame.customView.playerView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.quoridor.R
import com.example.quoridor.databinding.CustomViewMyPlayInfoBinding
import com.example.quoridor.ingame.frontDomain.FrontPlayer
import com.example.quoridor.ingame.utils.Func

class MyPlayInfoView: ConstraintLayout {

    private val binding: CustomViewMyPlayInfoBinding by lazy {
        CustomViewMyPlayInfoBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_my_play_info, this, false)
        )
    }

    val leftTimeTextView by lazy {
        binding.leftTimeTv
    }
    val leftWallTextView by lazy {
        binding.leftWallTv
    }

    constructor(context: Context): super(context) {
        initView()
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
        initView()
    }

    private fun initView() {
        addView(binding.root)
    }

    fun setPlayer(player: FrontPlayer) {
        leftTimeTextView.text = Func.millToMinSec(player.left_ms)
        val leftWallText = "${context.getString(R.string.left_wall_prefix)}${player.left_wall}"
        leftWallTextView.text = leftWallText
    }
}