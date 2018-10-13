package com.example.helloworld.chatapp.fragments


import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.helloworld.chatapp.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_otp.*


class OtpFragment : Fragment() {

    private var mVerificationId: String? = null
    internal lateinit var mFirebaseauth: FirebaseAuth
    internal lateinit var verificationCode: String
    lateinit var  phn : String
    lateinit var  mDatabaseReference : DatabaseReference
    private lateinit var  mFirebaseDatabase : FirebaseDatabase
    var  phoneLong : Long = 0
    var isConnected : Boolean = false


    lateinit var intent : Intent

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_otp, container, false)

        intent = Intent()
        mFirebaseauth = FirebaseAuth.getInstance();
        progressBar.visibility = View.INVISIBLE

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mDatabaseReference = mFirebaseDatabase.getReference().child("PhoneNumber")

        verify_button.setOnClickListener {

            verificationCode = otp_edittext.getText().toString();

            if(!(TextUtils.isEmpty(verificationCode))){

                progressBar.visibility = View.VISIBLE

                var  credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId!!, verificationCode);

            isConnected = checkInternetConnection()

                if (isConnected) {

                    //we are connected to a network



                    signInWithPhoneAuthCredential(credential);

                } else {


                    Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_SHORT).show();

                }
            } else {
                otp_edittext.setError("Please enter OTP");
            }

        }


        return view
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {


        mFirebaseauth.signInWithCredential(credential).addOnCompleteListener(activity!!, object : OnCompleteListener<AuthResult>{
            override fun onComplete(task: Task<AuthResult>){

                if (task.isSuccessful){

                  progressBar.visibility = View.INVISIBLE
                }
            }

        })



        }

    public fun   checkInternetConnection() : Boolean{

        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager



        var  networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnected()){

            return true;
        }
        else
            return false;

    }
    companion object {

        fun instantiate() : Fragment{

            return  OtpFragment()
        }
        var TAG : String = "OtpFragment"
    }


}






