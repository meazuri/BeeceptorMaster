package com.seint.beeceptor.view

import android.R
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface




fun showErrorDialog(context : Context , title: String, message : String){

    AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(R.string.ok,
            DialogInterface.OnClickListener { dialog, which ->
            })
        .setIcon(R.drawable.ic_dialog_alert)
        .show()
}