package com.example.quizkotlin
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ResultsActivity : AppCompatActivity(){

    private lateinit var logo: ImageView
    private lateinit var message: TextView
    private var scores : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_results)
        scores = intent.getSerializableExtra(SCORE_PASS) as Int
        Log.i("scores_", scores.toString())
        logo = findViewById(R.id.main_logo)
        message = findViewById(R.id.results)
        message.text = ("You scored: $scores")



        val sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim)

        logo.startAnimation(sideAnim)
        message.startAnimation(bottomAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            val loginIntent = Intent(this, StudentHomeActivity::class.java)
            startActivity(loginIntent)
            finish()
        }, 3000)

    }
    companion object {
        @SuppressLint("StaticFieldLeak")
        var SCORE_PASS = "scores"

    }
}
