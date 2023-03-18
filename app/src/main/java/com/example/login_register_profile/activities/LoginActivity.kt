package com.example.login_register_profile.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.login_register_profile.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    //viewbinding
    private  lateinit var binding: ActivityLoginBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progress diolog, will show  while login user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handel click , not  have account , goto register screen
        binding.noAccountTv.setOnClickListener {

            startActivity(Intent(this, RegisterActivity::class.java))
        }

        //handel click, begin login
        binding.loginBtn.setOnClickListener {

             validateData()
        }

    }

    private var email =""
    private var password =""

    private fun validateData() {
        //1.input data

        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //2.validate data

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

            Toast.makeText(this,"Invalid email form...", Toast.LENGTH_SHORT).show()

        }

        else if (password.isEmpty()){

            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show()
        }

        else{

            loginUser()
        }

    }

    private fun loginUser() {

        //3.login - firebase auth

        //show progress
        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                //login success
                checkUser()

            }

            .addOnFailureListener { e->

                //failed login
                progressDialog.dismiss()
                Toast.makeText(this,"Login Failed due to ${e.message}",Toast.LENGTH_SHORT).show()

            }


    }

    private fun checkUser() {

        //4. check user type - firebase auth

        progressDialog.setMessage("Checking User...")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {

                    progressDialog.dismiss()

                    //get user type
                    val userType = snapshot.child("userType").value

                    if(userType == "user"){

                        //customer
                        startActivity(Intent(this@LoginActivity, DashboardUserActivity::class.java))
                        finish()

                    }

                    else if(userType == "seller"){

                        startActivity(Intent(this@LoginActivity, DashboardSellerActivity::class.java))
                        finish()

                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}