package com.example.login_register_profile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.login_register_profile.databinding.ActivityDashboardSellerBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardSellerActivity : AppCompatActivity() {

    //view binding
    private  lateinit var binding: ActivityDashboardSellerBinding

    //firebase auth
    private lateinit var  firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardSellerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handel click , logout
        binding.logoutBtn.setOnClickListener{
            firebaseAuth.signOut()
            checkUser()
        }

        //handele click open profile
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this,ProfileActivity::class.java))
        }



    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser

        if(firebaseUser == null){

            //not logged in go to main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        else{
            //logged in , get and show user info
            val email = firebaseUser.email

            //set to text view of toolbar
            binding.subTitleTv.text = email

        }
    }
}