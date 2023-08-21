package com.example.quoridor

import android.R
import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.quoridor.databinding.ActivityPieChartTestBinding
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class PieChartTest : AppCompatActivity(){

    private val binding by lazy {
        ActivityPieChartTestBinding.inflate(layoutInflater)
    }

    private val TAG: String by lazy {
        getString(com.example.quoridor.R.string.Minseok_test_tag)
    }
    private var chart1: PieChart? = null

    // 파이 차트 설정
    private fun setPieChart() {
        chart1!!.clearChart()
        Log.d(TAG,"?")
        chart1!!.addPieSlice(PieModel("TYPE 1", 60f, Color.parseColor("#FF5454")))//Color.parseColor("#CDA67F")
        chart1!!.addPieSlice(PieModel("TYPE 2", 40f, Color.parseColor("#54A7FF")))
        chart1!!.startAnimation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d(TAG,"??")
        chart1 = binding.tab1Chart1
        setPieChart()
    }
}