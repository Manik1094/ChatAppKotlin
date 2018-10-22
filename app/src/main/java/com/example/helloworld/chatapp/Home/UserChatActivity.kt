package com.example.helloworld.chatapp.Home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.example.helloworld.chatapp.Adapters.ChatMessageAdapter
import com.example.helloworld.chatapp.Models.Chat
import com.example.helloworld.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_chat.*

class UserChatActivity : AppCompatActivity() {

    // Firebase
    lateinit var firebaseAuth : FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var databaseReference: DatabaseReference
    lateinit var currentUserId : String

    private lateinit var mAdapter : ChatMessageAdapter
    //private lateinit var edt_chat_message : EditText
    private lateinit var message : String
    private val TAG : String = "UserChatActivity"
    private lateinit var intent1: Intent
    private lateinit var receiverUid : String
    private  var chatList : ArrayList<Chat> = ArrayList()
   // private lateinit var btn_send : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_chat)

        intent1 = intent
        receiverUid = intent1.getStringExtra("receiverUserId")

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Chat")

        currentUserId = firebaseAuth.currentUser!!.uid

        var layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager


        btn_send.setOnClickListener {

            message = edt_message.text.toString()
            addChatDataToDb(message, receiverUid)

        }



    }

    private fun addChatDataToDb(message: String, receiverUid : String) {

       var chat = Chat(currentUserId, System.currentTimeMillis().toString(), "text", message)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled : $error")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var mRef : DatabaseReference = databaseReference.child(currentUserId).child(receiverUid).push()
                mRef.setValue(chat)
                chatList.add(chat)
                mAdapter = ChatMessageAdapter(applicationContext, chatList)
                recyclerView.adapter = mAdapter
            }

        })

    }
}
