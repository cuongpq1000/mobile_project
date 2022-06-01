package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    //ViewBinding
    private lateinit var binding: ActivitySignUpBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    //Firebase Database
    private lateinit var database: DatabaseReference

    private var email = ""
    private var password = ""
    private var firstName = ""
    private var lastName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure Actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Creating account...")
        progressDialog.setCanceledOnTouchOutside(false)

        //firebase authentication init
        firebaseAuth = FirebaseAuth.getInstance()

        //sign up button listener
        binding.signUpBtn.setOnClickListener {
            //validate data
            validateData()
        }

    }

    private fun validateData(){
        //get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        firstName = binding.firstNameEt.text.toString().trim()
        lastName = binding.lastNameEt.text.toString().trim()

        //export to database
        database = FirebaseDatabase.getInstance().getReference("Users")
        val user = User(firstName, lastName, email)
        database.child(firstName).setValue(user)

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.error = "Invalid email format"
        }else if(TextUtils.isEmpty(password)){
            //if no password entered
            binding.passwordEt.error = "Please enter password"
        }else if(password.length < 6){
            binding.passwordEt.error = "Password must be at least 6 characters"
        }else{
            //sign up accepted
            firebaseSignUp()
        }
    }

    private fun firebaseSignUp(){
        //show progress
        progressDialog.show()

        //create an account
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            //successful signup
            progressDialog.dismiss()

            //get user
            val firebaseUser = firebaseAuth.currentUser
            val email = firebaseUser!!.email
            Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()

            //open profile
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }.addOnFailureListener{e->
            progressDialog.dismiss()
            Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onSupportNavigateUp(): Boolean{
        //go back to previous activity when pressed
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}