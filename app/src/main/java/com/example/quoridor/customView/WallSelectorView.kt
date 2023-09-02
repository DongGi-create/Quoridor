package com.example.quoridor.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.quoridor.R
import com.example.quoridor.databinding.CustomViewWallSelectorBinding

class WallSelectorView: ConstraintLayout {

    private val binding: CustomViewWallSelectorBinding by lazy {
        CustomViewWallSelectorBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_wall_selector, this, false)
        )
    }

    val verticalWallChooseView by lazy {
        binding.verticalWallLayout
    }
    val verticalWallView by lazy {
        binding.verticalWall
    }
    val horizontalWallChooseView by lazy {
        binding.horizontalWallLayout
    }
    val horizontalWallView by lazy {
        binding.horizontalWall
    }
    val overlay by lazy {
        binding.chooseLayoutOverlay
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
}