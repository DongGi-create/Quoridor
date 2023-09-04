package com.example.quoridor.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.quoridor.R
import com.example.quoridor.databinding.CustomViewRankingBinding


/*
primary secondary constructor
secondary constructor은 primary에서만 가져오기 가능
class rankingItemView(context: Context) :ConstraintLayout(context){
constructor(context: Context, attrs: AttributeSet): this(context){
*/
class RankingItemView : ConstraintLayout {
    private val binding: CustomViewRankingBinding by lazy{
        CustomViewRankingBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_ranking,this,false)
        )
    }
    lateinit var ivRating: ImageView
    lateinit var tvRank: TextView
    lateinit var tvName: TextView
    lateinit var tvRating: TextView

    constructor(context: Context): super(context){
        initView()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        initView()
        setAttrs(attrs)
    }

    private fun initView() {
        addView(binding.root)
    }
    private fun setAttrs(attrs: AttributeSet){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RankingView)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        //R.styleable.MenuIconView_resourceId 이 뷰를 사용할때 호출 뒤의 hobanwoo_black은 default 값이다(설정 안해주면 넣는다)
        ivRating = binding.customIvRating
        tvRank = binding.customTvRank
        tvName = binding.customTvName
        tvRating = binding.customTvRating

        typedArray.recycle()
    }
}