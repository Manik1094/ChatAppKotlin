package com.example.helloworld.chatapp.Auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.helloworld.chatapp.Home.HomeActivity
import com.example.helloworld.chatapp.Models.User
import com.example.helloworld.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import java.io.ByteArrayOutputStream

class DataCollectionActiviy : AppCompatActivity() {

    lateinit var progressBar : ProgressBar
    lateinit var profile_photo : CircleImageView
    lateinit var name_edittext : EditText
    lateinit var status_edittext : EditText
    lateinit var register_button : Button

    // Firebase
    lateinit var mDatabase : FirebaseDatabase
    lateinit var mDatabaseReference : DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_collection_activiy)

        mDatabase = FirebaseDatabase.getInstance()

        mDatabaseReference = mDatabase.reference.child("Users")

        progressBar = findViewById(R.id.progress_profile)
        profile_photo = findViewById(R.id.profile_photo)
        name_edittext = findViewById(R.id.name_edittext)
        status_edittext = findViewById(R.id.status_edittext)
        register_button = findViewById(R.id.register_button)
        progressBar.visibility = View.GONE

        register_button.setOnClickListener {

            var users = User(name_edittext.text.toString() ,
                             status_edittext.text.toString() ,
                             convertProfileAvatarToBase64(),
                             FirebaseAuth.getInstance().uid!! )

            mDatabaseReference.child(FirebaseAuth.getInstance().currentUser!!.uid).addValueEventListener( object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {

                    Log.e("DataCollection" , "Database Error is ${p0.message}")

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.ref.setValue(users)
                    var intent = Intent(this@DataCollectionActiviy  , HomeActivity::class.java)
                    startActivity(intent)
                }

            } )



        }


    }

    private fun  convertProfileAvatarToBase64() : String{

        var bitmap =  BitmapFactory.decodeResource(resources, R.drawable.profile_avatar)
        Log.e("Data", bitmap.toString())
        var baos: ByteArrayOutputStream =  ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos)
        var  b =baos.toByteArray()
        var temp =Base64.encodeToString(b, Base64.DEFAULT)

        return temp
    }


}
