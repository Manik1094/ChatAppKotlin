package com.example.helloworld.chatapp.Adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.helloworld.chatapp.Models.User
import com.example.helloworld.chatapp.R

class UsersListAdapter(context : Context, usersList : ArrayList<User>, listener : OnItemClickListener) : RecyclerView.Adapter<UsersListAdapter.MyViewHolder>() {

    private var mContext : Context
    private var mUsersList: ArrayList<User>
    private var mListener : OnItemClickListener

    init {
       mContext = context
        mUsersList = usersList
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {

        val view : View = LayoutInflater.from(mContext).inflate(R.layout.user_list_item, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {
        return mUsersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.userName.text = mUsersList.get(position).name
        holder.userStatus.text = mUsersList.get(position).status

         var decodedString = Base64.decode(mUsersList.get(position).profile_photo, Base64.DEFAULT)
         var decodedByte : Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

        Glide.with(mContext).load(decodedByte).into(holder.userImage)

        holder.itemView.setOnClickListener {
            mListener.onItemClicked(position)
        }

    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var userName : TextView
        var userStatus : TextView
        var userImage : ImageView

        init {

            userName = itemView.findViewById(R.id.user_name)
            userStatus = itemView.findViewById(R.id.user_status)
            userImage = itemView.findViewById(R.id.user_image)

        }



    }


}