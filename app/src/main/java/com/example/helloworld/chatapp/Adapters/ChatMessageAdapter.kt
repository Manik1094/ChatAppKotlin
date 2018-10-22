package com.example.helloworld.chatapp.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.helloworld.chatapp.Models.Chat
import com.example.helloworld.chatapp.R
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

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.tvChatText.text = chatList[position].message
        holder.tvChatTime.text = chatList[position].time

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tvChatText : TextView
        var tvChatTime : TextView

        init {
            tvChatText = itemView.findViewById(R.id.tv_chat_text)
            tvChatTime = itemView.findViewById(R.id.tv_chat_time)
        }

    }


}