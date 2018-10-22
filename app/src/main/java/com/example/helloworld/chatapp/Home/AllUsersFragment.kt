package com.example.helloworld.chatapp.Home


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.example.helloworld.chatapp.Adapters.UsersListAdapter
import com.example.helloworld.chatapp.Models.User

import com.example.helloworld.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AllUsersFragment : Fragment() {

    lateinit var mAdapter : UsersListAdapter
    lateinit var recyclerView : RecyclerView
     var usersList : ArrayList<User> = ArrayList()

    lateinit var pbBar : ProgressBar

    // Firebase
    lateinit var mDatabase : FirebaseDatabase
    lateinit var mDatabaseRef : DatabaseReference



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_all_users, container, false)

        pbBar = view.findViewById(R.id.progressBar)


        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseRef = mDatabase.reference

        recyclerView = view.findViewById(R.id.recyclerView)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(activity!!.applicationContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        loadAllUsers()

       return view
    }

    private fun loadAllUsers() {

        val query : Query = mDatabaseRef.child("Users")
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

                Log.e("ALlUsersFragment", "onCancelled : ${error.message}")
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for ( ds : DataSnapshot in dataSnapshot.children ) {

                    var user = ds.getValue(User::class.java)
                    if (ds.key != FirebaseAuth.getInstance().currentUser!!.uid) {
                        usersList.add(user!!)
                    }

                }

                mAdapter = UsersListAdapter(context!!, usersList, object : UsersListAdapter.OnItemClickListener {
                    override fun onItemClicked(position: Int) {

                        val intent = Intent(activity, UserChatActivity::class.java)
                        intent.putExtra("receiverUserId", usersList[position].uid)
                        startActivity(intent)

                    }

                })
                pbBar.visibility = View.GONE
                recyclerView.adapter = mAdapter


            }


        })

    }


}
