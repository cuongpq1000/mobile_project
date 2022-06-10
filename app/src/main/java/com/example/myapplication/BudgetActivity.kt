package com.example.myapplication

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class BudgetActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var database: DatabaseReference
    private lateinit var loader: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget)
        val button = findViewById<FloatingActionButton>(R.id.fab)

        button.setOnClickListener {
            addItem();
        }
    }

    private fun addItem() {
        val alert = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val myView = inflater.inflate(R.layout.input, null)
        alert.setView(myView)

        val dialog = alert.create()
        dialog.setCancelable(false)

        val spinner = myView.findViewById<Spinner>(R.id.spinner)
        val amount = myView.findViewById<EditText>(R.id.amount)
        val cancel = myView.findViewById<Button>(R.id.cancel)
        val save = myView.findViewById<Button>(R.id.save)

        save.setOnClickListener {
            val budgetAmount = amount.text.toString()
            val budgetItem = spinner.selectedItem.toString()
            if(TextUtils.isEmpty(budgetAmount)){
                amount.setError("Amount is required")
            }
            if(budgetItem.equals("Select an Item")){
                Toast.makeText(this, "Select a valid item", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()


    }
}