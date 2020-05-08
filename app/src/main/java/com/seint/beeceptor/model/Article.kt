package com.seint.beeceptor.model

import java.io.Serializable

data class Article (

    var id : Int,
    var title : String,
    var last_update : String ,
    var short_description : String,
    var avatar : String,
    var text : String
) : Serializable