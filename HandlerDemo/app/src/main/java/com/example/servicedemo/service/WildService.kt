package com.example.servicedemo.service

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import android.widget.Toast

/**
 * startService启动 模拟下载野Service
 */
class WildService : Service() {

    var TAG ="WildService"

    lateinit var flag: String

    var isCreate  = false

    var handler: Handler = Handler()



    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        isCreate=true

        Log.d(TAG,"onCreate")

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d(TAG,"onStartCommand")

        Log.d("onStartComm Thread-name",Thread.currentThread().name)

        flag = intent?.getStringExtra("flag")!!

        if (TextUtils.equals(flag,"start")){

            if (isCreate) handleData() else toast(" already loading ")

            isCreate= false

        }else {
            stopSelf()
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG,"onDestroy")
    }

    /**
     * download
     */
    private fun handleData() {

          Runnable {

              Log.d(TAG,"Runnable")

              Log.d("handleData Thread-name",Thread.currentThread().name)

             loop@ for (i in 1..10) {

                 handler.post {toast("load $i %")}
             }

              handler.post {toast("load success")}

         }.run()


     }

    private fun toast(s: String){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show()
    }


}