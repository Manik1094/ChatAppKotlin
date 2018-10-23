package com.example.helloworld.chatapp.Models

class Chat(from : String, time : String, type : String, message : String) {

     var from : String
     var time : String
     var type : String
     var message : String

    init {
        this.from = from
        this.time = time
        this.type = type
        this.message = message
    }

    constructor() : this(""  , "" , "", "")


}