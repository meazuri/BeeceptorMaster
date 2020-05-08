package com.seint.beeceptor

import java.text.SimpleDateFormat
import java.util.*

fun Date.formatDate(format: String) :String{

    var time = ""

    if (format.isBlank()){
        val sdf = SimpleDateFormat(format)
        time +=  sdf.format(this)

    }else{
        val sdf = SimpleDateFormat("HH:mm d MMM, yyyy")
        time += sdf.format(this)
    }
    return time
}
