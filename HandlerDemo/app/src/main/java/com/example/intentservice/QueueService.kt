package com.example.intentservice

import android.app.IntentService
import android.content.Intent
import android.util.Log

class QueueService : IntentService("QueueService") {
    var  i = 0



    override fun onHandleIntent(p0: Intent?) {
        Log.d("QueueService","onHandleIntent$i")
        Log.d("QueueService",Thread.currentThread().name)
        i++
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
}