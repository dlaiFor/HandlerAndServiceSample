package com.example.bind

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

// issue

// 1 val var

// 2  object：ServiceConnection？

// 3 with let also apply  ->https://blog.csdn.net/ljd2038/article/details/79576091

// 4 原来类后面跟的括号是构造器6666

class MainActivity : AppCompatActivity() {

    val TAG :String = "MainActivity"

    private lateinit var mService : NormalService

    private  var mBounds : Boolean = false

    /** Defines callbacks for service binding, passed to bindService()  */
    private val connection  = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName?, service: IBinder?) {
            // We've bound to NormalBinder, cast the IBinder and get NormalBinder instance
            val binder = service as NormalService.NormalBinder

            mService = binder.getService()

            mBounds = true

            Log.d(TAG,"onServiceConnected success" )
        }

        override fun onServiceDisconnected(className: ComponentName?) {
            Log.d(TAG,"onServiceConnected disconnect" )
            mBounds = false

        }


    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bind.setOnClickListener {
            Intent(this,NormalService::class.java)
                .also { intent-> bindService(intent,connection, Service.BIND_AUTO_CREATE) }
        }

        invoke.setOnClickListener {
            if (mBounds)
            number.text = mService.randomNumber.toString()
        }

        unbind.setOnClickListener {
            mBounds = false

            unbindService(connection)
        }


    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG,"onStop unbindService" )
        mBounds = false

        unbindService(connection)
    }
}
