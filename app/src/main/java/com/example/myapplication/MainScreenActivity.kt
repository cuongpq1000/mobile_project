package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class MainScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        val setting = findViewById<CardView>(R.id.settingCard)
        val budget = findViewById<CardView>(R.id.budgetCard)
        setting.setOnClickListener{
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        budget.setOnClickListener {
            var intent = Intent(this, BudgetActivity::class.java)
            startActivity(intent)
        }
    }
}