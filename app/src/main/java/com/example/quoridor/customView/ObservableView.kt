package com.example.quoridor.customView

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

abstract class ObservableView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr) {

    //a list to hold the list of observers and their LiveData
    private val autoObservers = ArrayList<Pair<LiveData<*>, Observer<*>>>()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        for((liveData, observer) in autoObservers){
            liveData.observeForever(observer as Observer<in Any>)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        for((liveData, observer) in autoObservers){
            liveData.removeObserver(observer as Observer<in Any>)
        }
    }

    protected fun<T : Any> LiveData<T>.observe( observer: Observer<T> ){
        autoObservers.add(this to observer)

        //if it's not attached, onAttachedToWindow will do the observation
        if(isAttachedToWindow){
            observeForever(observer)
        }
    }
}