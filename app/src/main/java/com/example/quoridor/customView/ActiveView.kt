//package com.example.quoridor.customView
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import android.view.ViewGroup
//import android.widget.LinearLayout
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.constraintlayout.widget.ConstraintSet
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.Observer
//
//abstract class ActiveView: ConstraintLayout {
//
//    constructor(context: Context): super(context) {
//        val o = View(context)
//        o.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
//        o.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
//        this.const
//        addView()
//    }
//    constructor(context: Context, attrs: AttributeSet): super(context, attrs) {
//        initView()
//    }
//
//    override fun addView(child: View?) {
//        val cs = ConstraintSet()
//        cs.connect(child!!.id, ConstraintSet.START, this.id, ConstraintSet.START)
//        cs.connect(child!!.id, ConstraintSet.END, this.id, ConstraintSet.START)
//        cs.connect(child!!.id, ConstraintSet.TOP, this.id, ConstraintSet.START)
//        cs.connect(child!!.id, ConstraintSet.BOTTOM, this.id, ConstraintSet.START)
//        cs.applyTo(this)
//        super.addView(child)
//    }
//
//
//}