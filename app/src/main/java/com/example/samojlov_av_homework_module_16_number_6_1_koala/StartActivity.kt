package com.example.samojlov_av_homework_module_16_number_6_1_koala

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.samojlov_av_homework_module_16_number_6_1_koala.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var startImageAnimIV: ImageView
    private lateinit var startTextAnimTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_start)
        supportActionBar?.hide()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        init()
        animation()
    }
    private fun init(){
        startImageAnimIV = binding.startImageAnimIV
        startTextAnimTV = binding.startTextAnimTV
    }
    private fun animation() {

        val animation1 = AnimationUtils.loadAnimation(this, R.anim.start_anim)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.start_anim)
        val animation3 = AnimationUtils.loadAnimation(this, R.anim.start_anim)
        val animation4 = AnimationUtils.loadAnimation(this, R.anim.text_anim)
        val animation5 = AnimationUtils.loadAnimation(this, R.anim.duration_anim)

        val animation1Listener: Animation.AnimationListener =
            object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    startImageAnimIV.setImageResource(R.drawable.two)
                    startImageAnimIV.startAnimation(animation2)
                }

                override fun onAnimationRepeat(animation: Animation) {
                }

                override fun onAnimationStart(animation: Animation) {
                }
            }
        val animation2Listener: Animation.AnimationListener =
            object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    startImageAnimIV.setImageResource(R.drawable.three)
                    startImageAnimIV.startAnimation(animation3)
                }

                override fun onAnimationRepeat(animation: Animation) {
                }

                override fun onAnimationStart(animation: Animation) {
                }
            }
        val animation3Listener: Animation.AnimationListener =
            object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    startImageAnimIV.visibility = View.INVISIBLE
                    startTextAnimTV.visibility = View.VISIBLE
                    startTextAnimTV.startAnimation(animation4)
                }

                override fun onAnimationRepeat(animation: Animation) {
                }

                override fun onAnimationStart(animation: Animation) {
                }
            }
        val animation4Listener: Animation.AnimationListener =
            object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    startTextAnimTV.startAnimation(animation5)
                }

                override fun onAnimationRepeat(animation: Animation) {
                }

                override fun onAnimationStart(animation: Animation) {
                }
            }
        val animation5Listener: Animation.AnimationListener =
            object : Animation.AnimationListener {
                override fun onAnimationEnd(animation: Animation) {
                    startActivity(Intent(this@StartActivity,MainActivity::class.java))
                }

                override fun onAnimationRepeat(animation: Animation) {
                }

                override fun onAnimationStart(animation: Animation) {
                }
            }

        animation1.setAnimationListener(animation1Listener)
        animation2.setAnimationListener(animation2Listener)
        animation3.setAnimationListener(animation3Listener)
        animation4.setAnimationListener(animation4Listener)
        animation5.setAnimationListener(animation5Listener)

        startImageAnimIV.startAnimation(animation1)
    }

}