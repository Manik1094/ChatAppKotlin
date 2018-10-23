package com.example.helloworld.chatapp.Home

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Toast
import com.example.helloworld.chatapp.Adapters.ChatMessageAdapter
import com.example.helloworld.chatapp.Models.Chat
import com.example.helloworld.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_user_chat.*
import java.util.*
import kotlin.collections.HashMap

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


        mAdapter = ChatMessageAdapter(applicationContext, chatList)
        recyclerView.adapter = mAdapter

        loadOurMessages()

        loadOppositeMessages()

        btn_send.setOnClickListener {

            Log.e(TAG , "Inside ONCLICKLISTENER")

            message = edt_message.text.toString()
            addChatDataToDb(message, receiverUid)

        }






    }

    private fun loadOppositeMessages() {

        var query : Query = databaseReference
                            .child(receiverUid)
                            .child(currentUserId)

        query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children){


                    var  chat : Chat = Chat()
                    var chatMap : HashMap<String , Objects> = ds.value as HashMap<String, Objects>

                    // Toast.makeText(applicationContext , "ChatMap size : "+chatMap.size , Toast.LENGTH_SHORT).show()
                    chat.from = chatMap.get("from").toString()
                    chat.message = chatMap.get("message").toString()
                    chat.time = chatMap.get("time").toString()
                    chat.type = chatMap.get("type").toString()


                    chatList.add(chat)
                    Toast.makeText(applicationContext , "ChatList size : "+chatList.size , Toast.LENGTH_LONG).show()


                    mAdapter.notifyDataSetChanged()
                }


            }

        })



    }

    private fun loadOurMessages() {

       var query : Query =  databaseReference.child(currentUserId).child(receiverUid)

        query.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(ds: DataSnapshot, p1: String?) {

                var  chat : Chat = Chat()
                var chatMap : HashMap<String , Objects> = ds.value as HashMap<String, Objects>

                // Toast.makeText(applicationContext , "ChatMap size : "+chatMap.size , Toast.LENGTH_SHORT).show()
                chat.from = chatMap.get("from").toString()
                chat.message = chatMap.get("message").toString()
                chat.time = chatMap.get("time").toString()
                chat.type = chatMap.get("type").toString()


                chatList.add(chat)
                Toast.makeText(applicationContext , "ChatList size : "+chatList.size , Toast.LENGTH_LONG).show()


                mAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })



       /** query.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children){


                    var  chat : Chat = Chat()
                    var chatMap : HashMap<String , Objects> = ds.value as HashMap<String, Objects>

                   // Toast.makeText(applicationContext , "ChatMap size : "+chatMap.size , Toast.LENGTH_SHORT).show()
                     chat.from = chatMap.get("from").toString()
                    chat.message = chatMap.get("message").toString()
                    chat.time = chatMap.get("time").toString()
                    chat.type = chatMap.get("type").toString()


                     chatList.add(chat)
                    Toast.makeText(applicationContext , "ChatList size : "+chatList.size , Toast.LENGTH_LONG).show()


                    mAdapter.notifyDataSetChanged()
                }
            }


        }) **/

    }

    override fun onStart() {
        super.onStart()
       // if (chatList!=null){

         //   Log.e(TAG , "Inside not null condition , size of arraylist : " + chatList.size)


        //}
    }

    private fun addChatDataToDb(message: String, receiverUid : String) {



        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled : $error")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var chat = Chat(currentUserId, System.currentTimeMillis().toString(), "text", message)

                var mRef : DatabaseReference = databaseReference.child(currentUserId).child(receiverUid).push()
                mRef.setValue(chat)
              //  chatList.add(chat)
                //mAdapter.notifyDataSetChanged()




                Toast.makeText(applicationContext , "Size of arraylist : " + chatList.size , Toast.LENGTH_LONG).show()
                Log.e(TAG  , "INside onDataChange , Size of Arraylist : " + chatList.size)

            }

        })

    }
}
