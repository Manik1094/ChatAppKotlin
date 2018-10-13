package com.example.helloworld.chatapp.Auth


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast

import com.example.helloworld.chatapp.R
import com.example.helloworld.chatapp.utils.NetworkUtils
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
    lateinit var signUpBtn : Button
    lateinit var progressBar : ProgressBar


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var  view =  inflater.inflate(R.layout.fragment_phone, container, false)

        mFirebaseauth = FirebaseAuth.getInstance()

        progressBar = view.findViewById(R.id.progressbar)

        progressBar.visibility = View.INVISIBLE

        signUpBtn = view.findViewById(R.id.signup_button)



       signUpBtn.setOnClickListener {

           isConnected = NetworkUtils.checkInternetConnection(context!!)

           if (isConnected){

               phonenumber = phone_edittext.text.toString()
               phonenumber = "+91" + phonenumber

               authenticateUser(phonenumber)


           }
           else{
               Toast.makeText(activity, "No Internet Connection", Toast.LENGTH_LONG).show()

           }

       }


        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(p0: PhoneAuthCredential?) {

                Log.e(TAG, "Entered method onVerificationCompleted")
            }

            override fun onVerificationFailed(e: FirebaseException?) {

                progressbar.visibility = View.INVISIBLE
                Toast.makeText(activity, "Error occurred! Please try again", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Entered method onVerificationFailed", e)
            }

            override fun onCodeSent(verificationId: String?, token: PhoneAuthProvider.ForceResendingToken?) {

                progressbar.visibility = View.INVISIBLE

                Log.e(TAG, "Entered method onCodeSent")

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId!!
                Log.e(TAG, "Verification Id is " + mVerificationId)
                mResendToken = token!!

                val otpFragment  = OtpFragment()
                val args = Bundle()
                args.putString("phoneNumber", phonenumber)
                args.putString("verificationId", mVerificationId)
                otpFragment.arguments = args

                Log.e(TAG, "Phone Number Sending: $phonenumber")

                val transaction : FragmentTransaction = activity!!.supportFragmentManager.beginTransaction()

                transaction.replace(R.id.frame_auth, otpFragment)
                if (!TextUtils.isEmpty(tag)) {
                    transaction.addToBackStack(tag)
                }

                transaction.commit()


           /**     val intent =  Intent(activity, OtpFragment::class.java)
                intent.putExtra("phoneNumber", phonenumber)
                intent.putExtra("verificationId", mVerificationId)
                startActivity(intent)
                requireActivity().finish() */

            }

        }

        return view
    }

    private fun  authenticateUser(phonenumber : String){

      //  if (phonenumber.length == 10) {

           progressbar.visibility = View.VISIBLE
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phonenumber, 60, TimeUnit.SECONDS,activity!!, mCallbacks)

    }


    companion object {

         fun instantiate() : Fragment{

            return PhoneFragment()
        }
    }


}
