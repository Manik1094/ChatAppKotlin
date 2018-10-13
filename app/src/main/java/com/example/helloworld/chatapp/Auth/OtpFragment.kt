package com.example.helloworld.chatapp.Auth


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast

import com.example.helloworld.chatapp.R
import com.example.helloworld.chatapp.Home.HomeActivity
import com.example.helloworld.chatapp.utils.NetworkUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_otp.*


class OtpFragment : Fragment() {

     var mVerificationId: String? = ""
    val TAG : String = "OtpFragment"
    internal lateinit var mFirebaseauth: FirebaseAuth
    internal lateinit var verificationCode: String
    lateinit var  phn : String
    lateinit var  mDatabaseReference : DatabaseReference
    private lateinit var  mFirebaseDatabase : FirebaseDatabase
    var  phoneLong : Long = 0
    var isConnected : Boolean = false
    private lateinit var phoneNumber : String

     var args : Bundle? = null
    lateinit var mProgressBar : ProgressBar
    lateinit var verifyBtn : Button
    //lateinit var intent : Intent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_otp, container, false)

        mProgressBar = view.findViewById(R.id.progressBar)
        verifyBtn = view.findViewById(R.id.verify_button)

        args = this.arguments
        phoneNumber = args!!.getString("phoneNumber")
        Log.e(TAG, "phone number received: $phoneNumber")

        mVerificationId = args!!.getString("verificationId")


     //   intent = Intent()
       // phoneNumber = intent.getStringExtra("phoneNumber")
        mFirebaseauth = FirebaseAuth.getInstance()
        mProgressBar.visibility = View.INVISIBLE

        mFirebaseDatabase = FirebaseDatabase.getInstance()

        mDatabaseReference = mFirebaseDatabase.reference.child("PhoneNumber")

        verifyBtn.setOnClickListener {

            verificationCode = otp_edittext.text.toString()

            if(!(TextUtils.isEmpty(verificationCode))){

                progressBar.visibility = View.VISIBLE

                Log.e(TAG, "Verification Id : $mVerificationId")
                Log.e(TAG, "Verification code : $verificationCode")


                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId!!, verificationCode)

            isConnected = NetworkUtils.checkInternetConnection(context!!)

                if (isConnected) {

                    //we are connected to a network

                    signInWithPhoneAuthCredential(credential)

                } else {

                    Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }

            } else {
                otp_edittext.error = "Please enter OTP"
            }

        }

        return view
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {


        mFirebaseauth.signInWithCredential(credential).addOnCompleteListener(activity!!) { task ->
            if (task.isSuccessful){
                progressBar.visibility = View.INVISIBLE

                mDatabaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "onCancelled: $error")
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        if (dataSnapshot.hasChild(phoneNumber)) {

                            val intent = Intent(activity, HomeActivity::class.java)
                            startActivity(intent)

                        } else {



                            mDatabaseReference.child(phoneNumber).setValue(mFirebaseauth.currentUser!!.uid)
                            val intent = Intent(activity, DataCollectionActiviy::class.java)
                            startActivity(intent)
                        }
                    }
                })
            }
        }

    }





}






