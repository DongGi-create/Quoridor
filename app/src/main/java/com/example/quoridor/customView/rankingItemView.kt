package com.example.quoridor.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.example.quoridor.R
import com.example.quoridor.customView.playerView.Player
import com.example.quoridor.databinding.CustomViewRankingBinding
import com.example.quoridor.util.Func

/*
primary secondary constructor
secondary constructor은 primary에서만 가져오기 가능
class rankingItemView(context: Context) :ConstraintLayout(context){
constructor(context: Context, attrs: AttributeSet): this(context){
*/
class rankingItemView : ConstraintLayout {
    private val binding: CustomViewRankingBinding by lazy{
        CustomViewRankingBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_ranking,this,false)
        )
    }
    lateinit var medalIv: ImageView
    lateinit var rankTv: TextView
    lateinit var nameTv: TextView
    lateinit var ratingTv:TextView

    constructor(context: Context): super(context){
        initView()
    }

    constructor(context: Context, attrs: AttributeSet): this(context){
        initView()
        setAttrs(attrs)
    }

    private fun initView() {
        removeAllViews() // 이전 자식 뷰들을 모두 제거
        addView(binding.root)
    }
    private fun setAttrs(attrs: AttributeSet){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RankingView)
        setTypeArray(typedArray)
    }
    private fun setTypeArray(typedArray: TypedArray) {
        val sourceId = typedArray.getResourceId(R.styleable.RankingView_medalId,R.drawable.hobanwoo_black)
        val ranktext = typedArray.getString(R.styleable.RankingView_rank)
        val nametext = typedArray.getString(R.styleable.RankingView_name)
        val ratingtext = typedArray.getString(R.styleable.RankingView_rating)
        //R.styleable.MenuIconView_resourceId 이 뷰를 사용할때 호출 뒤의 hobanwoo_black은 default 값이다(설정 안해주면 넣는다)

        medalIv = binding.customRatingImageView
        rankTv = binding.customRankingTextView
        nameTv = binding.customNameTextView
        ratingTv = binding.customRatingTextView
        typedArray.recycle()
    }
}