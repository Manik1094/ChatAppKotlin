package com.example.helloworld.chatapp.Adapters

import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.helloworld.chatapp.Models.Chat
import com.example.helloworld.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chat_list.view.*

class ChatMessageAdapter(context : Context, chatList : ArrayList<Chat>) : RecyclerView.Adapter<ChatMessageAdapter.MyViewHolder>() {

     var context : Context
     var chatList: ArrayList<Chat>

    init {
        this.context = context
        this.chatList = chatList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        var view : View = LayoutInflater.from(context).inflate(R.layout.chat_list, parent, false)
        return MyViewHolder(view)

    }

    override fun getItemCount(): Int {

        return chatList.size

    }

     fun submitList(newList : ArrayList<Chat>) {
        chatList = newList
         notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        var params : RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT )


        if(FirebaseAuth.getInstance().currentUser!!.uid.equals(chatList[position].from)) {

            holder.cvContainer.setCardBackgroundColor(context.getColor(R.color.lightGreen))
            params.addRule(RelativeLayout.ALIGN_RIGHT)
           // params.gravity = Gravity.RIGHT
            holder.cvContainer.layoutParams = params

        } else{
            holder.cvContainer.setCardBackgroundColor(context.getColor(R.color.lightGrey))
            params.addRule(RelativeLayout.ALIGN_LEFT)
            holder.cvContainer.layoutParams = params

        }

        holder.tvChatText.text = chatList[position].message
        holder.tvChatTime.text = chatList[position].time



    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvChatText : TextView
        var tvChatTime : TextView
        var cvContainer : CardView

        init {
            tvChatText = itemView.findViewById(R.id.tv_chat_text)
            tvChatTime = itemView.findViewById(R.id.tv_chat_time)
            cvContainer = itemView.findViewById(R.id.cv_container)
        }

    }


}