package com.example.quoridor.customView

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.quoridor.R
import com.example.quoridor.databinding.CustomViewMenuIconBinding

class MenuIconView : LinearLayout {

    private val binding: CustomViewMenuIconBinding by lazy{
        CustomViewMenuIconBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.custom_view_menu_icon,this,false)
        )
    }
    constructor(context: Context): super(context){
        initView()
    }

    constructor(context: Context, attrs: AttributeSet): super(context, attrs){
        initView()
        setAttrs(attrs)
    }

    private fun initView(){
        addView(binding.root)
    }
    private fun setAttrs(attrs: AttributeSet){
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuIconView)
        setTypeArray(typedArray)
    }

    private fun setTypeArray(typedArray: TypedArray) {
        val sourceId = typedArray.getResourceId(R.styleable.MenuIconView_resourceId,R.drawable.hobanwoo_black)
        val text = typedArray.getString(R.styleable.MenuIconView_icon_text)
        binding.icIv.setImageResource(sourceId)
        binding.icTv.text = text
        typedArray.recycle()
    }
}