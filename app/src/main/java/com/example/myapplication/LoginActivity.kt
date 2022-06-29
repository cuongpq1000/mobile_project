package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //ViewBinding
    private lateinit var binding:ActivityLoginBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //Progress Dialog
    private lateinit var progressDialog: ProgressDialog

    //Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    private var count = 0
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)

        //firebase authentication init
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()


        //handle registering button
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        //handling the sign in button
        binding.loginBtn.setOnClickListener{
            //validate data before login in
            validateData()
        }
    }
    private fun validateData(){
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        }else if(TextUtils.isEmpty(password)){
            //if no password entered
            binding.passwordEt.error = "Please enter password"
        }else{
            //data validated
            firebaseLogin()
        }
    }
    private fun firebaseLogin(){
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            //when login successful
            progressDialog.dismiss()
            //get user info
            val firebaseUser = firebaseAuth.currentUser
            val email = firebaseUser!!.email
            Toast.makeText(this, "Login as $email", Toast.LENGTH_SHORT).show()

            //open profile
            startActivity(Intent(this, MainScreenActivity::class.java))
            finish()
        }.addOnFailureListener { e->
            //when login fail
            progressDialog.dismiss()
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkUser(){
        //if user already logged in, go straight to profile activity
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        //if user is already logged in
//        if(firebaseUser != null){
//            startActivity(Intent(this, ProfileActivity::class.java))
//            finish()
//        }
    }
}