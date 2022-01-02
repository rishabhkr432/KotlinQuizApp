package com.example.quizkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.quizkotlin.models.Score
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


class ResultsActivity : AppCompatActivity() {

    private var Total: Int = 10
    private var correctAnswers: Int = 0
    private var wrongAnswers: Int = 0
    private lateinit var barChart: BarChart
    private lateinit var goBackButton: ImageView
    private var scoreList = ArrayList<Score>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)
        initViews()


        correctAnswers = intent.getSerializableExtra(SCORE_PASS) as Int

        wrongAnswers = Total - correctAnswers
        Log.i("correctAnswers", correctAnswers.toString())
        Log.i("wrongAnswers", wrongAnswers.toString())

        scoreList.add(Score("Correct Answers", correctAnswers))
        scoreList.add(Score("Wrong Answers", wrongAnswers))

        initBarChart()


        //now draw bar chart with dynamic data
        val entries: ArrayList<BarEntry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in scoreList.indices) {
            val score = scoreList[i]
            entries.add(BarEntry(i.toFloat(), score.score.toFloat()))
        }

        val barDataSet = BarDataSet(entries, "")
        barDataSet.valueFormatter = DefaultValueFormatter(0)
        barDataSet.barBorderWidth = 1f
        barDataSet.valueFormatter
//        val barDataSet1 = BarDataSet(entries, "Wrong Answers")


        barDataSet.setColors(
            resources.getColor(R.color.chartreuse),
            resources.getColor(R.color.light_red)
        )
        barDataSet.valueTextSize = 14f
        val data = BarData(barDataSet)
        barChart.data = data
        barChart.invalidate()

        goBackButton.setOnClickListener {
            val intent = Intent(this, StudentHomeActivity::class.java)
            startActivity(intent)
            finish()

        }

    }


    private fun initViews() {
        barChart = findViewById(R.id.barChart)
        goBackButton = findViewById(R.id.results_goBackButton)


    }


    private fun initBarChart() {


//        hide grid lines
        barChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = barChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)
        xAxis.textSize=16f



        //remove right y-axis
        barChart.axisRight.isEnabled = false
        barChart.axisLeft.isEnabled = false

        barChart.legend.isEnabled = false


        //remove description label
        barChart.description.isEnabled = false


        //add animation
        barChart.animateY(3000)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()

        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
//        xAxis.labelRotationAngle = +90f

    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            Log.d("Results", "getAxisLabel: index $index")
            return if (index < scoreList.size) {
                scoreList[index].answerType
            } else {
                ""
            }
        }
    }


    // simulate api call
    // we are initialising it directly
//    private fun getScoreList(): ArrayList<Score> {
//        scoreList.add(Score("John", 56))
//        scoreList.add(Score("Rey", 75))
//
//
//        return scoreList




//{
//            val loginIntent = Intent(this, StudentHomeActivity::class.java)
//            startActivity(loginIntent)
//            finish()





    companion object {
        @SuppressLint("StaticFieldLeak")
        var SCORE_PASS = "scores"
        private const val TAG = "ResultsActivity"

    }
}
