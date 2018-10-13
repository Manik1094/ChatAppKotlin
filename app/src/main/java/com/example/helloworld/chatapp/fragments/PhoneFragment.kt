package com.example.helloworld.chatapp.fragments


import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat.getSystemService
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.helloworld.chatapp.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_phone.*
import java.util.concurrent.TimeUnit


class PhoneFragment : Fragment() {

    private lateinit var  mVerificationId : String
    lateinit var mResendToken : PhoneAuthProvider.ForceResendingToken
    lateinit var mCallbacks : PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var mFirebaseauth : FirebaseAuth
    lateinit var  phonenumber : String
    var TAG : String = "PhoneFragment"
    var isConnected : Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var  view =  inflater.inflate(R.layout.fragment_phone, container, false)

        mFirebaseauth = FirebaseAuth.getInstance()

       signup_button.setOnClickListener {

           isConnected = checkInternetConnection()

           if (isConnected){

               phonenumber = phone_edittext.text.toString()
               phonenumber+= "+91";

               authenticateUser(phonenumber);


           }
           else{
               Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();

           }

       }

     /**   mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.e(TAG, "Entered method onVerificationCompleted");


            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Error occurred! Please try again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Entered method onVerificationFailed", e);


            }

            @Override
            public void onCodeSent(String verificationId,
                PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                progressBar.setVisibility(View.INVISIBLE);

                Log.e(TAG, "Entered method onCodeSent");

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                Log.e(TAG, "Verification Id is " + mVerificationId);
                mResendToken = token;

                verifyUser(mVerificationId , mResendToken);



            }


        };  **/

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {

                Log.e(TAG, "Entered method onVerificationCompleted")
            }

            override fun onVerificationFailed(e: FirebaseException?) {

                progressbar.visibility = View.INVISIBLE
                Toast.makeText(getActivity(), "Error occurred! Please try again", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Entered method onVerificationFailed", e);
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {

                progressbar.visibility = View.INVISIBLE

                Log.e(TAG, "Entered method onCodeSent");

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId!!;
                Log.e(TAG, "Verification Id is " + mVerificationId);
                mResendToken = token!!;

                var  intent =  Intent(activity, OtpFragment::class.java);
                intent.putExtra("phoneNumber", phonenumber);
                intent.putExtra("verificationId", mVerificationId);
                startActivity(intent);
                requireActivity().finish();

            }

        }

        return view;
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

    private fun  authenticateUser( phonenumber : String){

        if (phonenumber.length == 10) {

           progressbar.visibility = View.VISIBLE
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber, 60, TimeUnit.SECONDS,activity!!, mCallbacks);

        } else {
           phone_edittext.error = "Invalid Phone Number"
        }


    }


    companion object {

         fun instantiate() : Fragment{

            return  PhoneFragment()
        }
        var TAG : String = "PhoneFragment"
    }


}
