package com.example.helloworld.chatapp.activities


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.text.TextUtils
import com.example.helloworld.chatapp.R
import com.example.helloworld.chatapp.fragments.OtpFragment
import com.example.helloworld.chatapp.fragments.PhoneFragment
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    var CURRENT_FRAGMENT : String = " "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (savedInstanceState == null) {
            showFragment(PhoneFragment.instantiate());
        }

        button.setOnClickListener {

            if (!CURRENT_FRAGMENT.equals(OtpFragment.TAG)) {
                showFragment(OtpFragment.instantiate(), OtpFragment.TAG);
                CURRENT_FRAGMENT = OtpFragment.TAG;
            }

        }
    }

    private fun  showFragment(  fragment  : Fragment) {
        showFragment(fragment, "");
    }
    private fun  showFragment( fragment  : Fragment,  tag : String) {
         var transaction : FragmentTransaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frame_auth, fragment);
        if (!TextUtils.isEmpty(tag)) {
            transaction.addToBackStack(tag);
        }

        transaction.commit();
    }
}

