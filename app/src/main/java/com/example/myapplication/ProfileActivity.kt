package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.myapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class ProfileActivity : AppCompatActivity() {
    //View binding
    private lateinit var binding: ActivityProfileBinding
    //Action bar
    private lateinit var actionBar: ActionBar

    private lateinit var database: DatabaseReference

    //Firebase Authentication
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var uid: String
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure Action bar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        //firebase authentication inti
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Users")
        uid = firebaseAuth.currentUser?.uid.toString()
        Log.d("Cuong", uid)
        if(uid.isNotEmpty()){
            database.child(uid).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.getValue(User::class.java) != null){
                        user = snapshot.getValue(User::class.java)!!
                        binding.emailTv.text = user.firstName + " " + user.lastName
                    }
                    else{
                        Log.d("Cuong Pham", uid)
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ProfileActivity, "failed", Toast.LENGTH_SHORT).show()
                }

            })
        }

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
            database = FirebaseDatabase.getInstance().getReference("Users")


        }else{
            //user is not logged in, go to login activity
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }




}