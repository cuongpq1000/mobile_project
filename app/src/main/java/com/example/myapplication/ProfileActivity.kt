package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {
    //View binding
    private lateinit var binding: ActivityProfileBinding

    //Action bar
    private lateinit var actionBar: ActionBar

    //Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure Action bar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        //firebase authentication inti
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click when selecting log out button
        binding.logoutBtn.setOnClickListener(){
            firebaseAuth.signOut()
            checkUser()
        }

    }

    private fun checkUser(){
        //check whether the user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            //user is logged in, get user info
            val email = firebaseUser.email

            //set to text view
            binding.emailTv.text = email

        }else{
            //user is not logged in, go to login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}