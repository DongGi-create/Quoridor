package com.example.quoridor.customView.playerView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.quoridor.R
import com.example.quoridor.databinding.CustomViewOpPlayerInfoBinding
import com.example.quoridor.game.frontDomain.FrontPlayer
import com.example.quoridor.game.util.GameFunc
import com.example.quoridor.util.Func

class OpPlayerInfoView: ConstraintLayout {

    private val binding: CustomViewOpPlayerInfoBinding by lazy {
        CustomViewOpPlayerInfoBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_op_player_info, this, false)
        )
    }

    val leftTimeTextView by lazy {
        binding.leftTimeTv
    }
    val leftWallTextView by lazy {
        binding.leftWallTv
    }
    val playerNameTextView by lazy {
        binding.playerNameTv
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
        playerNameTextView.text = player.uid
    }
}