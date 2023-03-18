package com.example.login_register_profile.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.login_register_profile.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth = FirebaseAuth.getInstance()

        Handler().postDelayed({
            checkUser()
        },10) // show 1 sec
    }

    private fun checkUser() {

        //get current user ,if loggged in or not
        val firebaseUser = firebaseAuth.currentUser


        if(firebaseUser == null){

            //user not logged in , goto main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        else{

            //user logged in check user type ,some as done in login screen


            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(firebaseUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(snapshot: DataSnapshot) {



                        //get user type
                        val userType = snapshot.child("userType").value

                        if(userType == "user"){

                            //customer
                            startActivity(Intent(this@SplashActivity, DashboardUserActivity::class.java))
                            finish()

                        }

                        else if(userType == "seller"){

                            startActivity(Intent(this@SplashActivity, DashboardSellerActivity::class.java))
                            finish()

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {


                    }
                })



        }
    }
}