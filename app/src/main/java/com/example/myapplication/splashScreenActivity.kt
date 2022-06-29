package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class splashScreenActivity : AppCompatActivity() {
    private lateinit var image: ImageView
    private lateinit var text: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        var animation = AnimationUtils.loadAnimation(this, R.anim.animation)
        image = findViewById(R.id.image)
        text = findViewById(R.id.textView)
        image.animation = animation
        text.animation = animation

        Handler().postDelayed(Runnable {
            var intent = Intent(this@splashScreenActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}