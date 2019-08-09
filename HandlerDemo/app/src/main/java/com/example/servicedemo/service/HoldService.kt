package com.example.servicedemo.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

/**
 * bindService
 */
class HoldService :Service(){


    inner class  MyBinder : Binder() {
        
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}