package com.example.bind

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.util.*

class NormalService : Service() {

    private val TAG : String ="NormalService"

    // Binder given to clients
    private val binder = NormalBinder()

    // Random number generator
    private val mGenerator = Random()

    /** method for clients   重写了randomNumber的get方法 类型是Int大写的 有get*/
    val randomNumber: Int
        get() = mGenerator.nextInt(100)

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    inner class NormalBinder :Binder(){
        fun getService(): NormalService = this@NormalService
    }

    override fun onCreate() {
        super.onCreate()

    }
    override fun onBind(p0: Intent?): IBinder? {
        Log.d(TAG,"Service onBind" )
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d(TAG,"Service onUnbind" )
        return super.onUnbind(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"Service onDestroy" )
    }

}