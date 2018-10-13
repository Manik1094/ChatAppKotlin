package com.example.helloworld.chatapp.Models

class User(name : String , status : String , profile_photo : String )
{
    lateinit var name : String
    lateinit var status : String
    lateinit var profile_photo : String

    init {
        this.name = name
        this.status = status
        this.profile_photo = profile_photo
    }

}