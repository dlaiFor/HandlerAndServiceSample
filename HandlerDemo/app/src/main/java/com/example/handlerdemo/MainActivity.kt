package com.example.handlerdemo

import android.os.*
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var threadHandler: Handler

    private lateinit var handlerThread: HandlerThread

    var uiHandler  =   object  :Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            println("Current Thread-name 2 :"+Thread.currentThread().name)

        }
    }


    override fun onStart() {
        super.onStart()


        Thread(mRunnable).start()
        Thread(mRunnableTwo)
        Thread(mRunnableThree)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handlerThread = HandlerThread("myHandlerThread")

        handlerThread.start()

        var myHandler =Handler(handlerThread.looper)

        myHandler.post(Runnable {
            println("Current Thread-name 6 :"+Thread.currentThread().name)
        })

        var myHandler2 =Handler(Looper.getMainLooper())

        myHandler2.post(Runnable {
            println("Current Thread-name 7 :"+Thread.currentThread().name)
        })

    }

    //子线程
    private val mRunnable = Runnable {
        run {

            println("Current Thread-name 1 :"+Thread.currentThread().name)

            uiHandler.sendEmptyMessage(0)

            uiHandler.post(Runnable {
                println("Current Thread-name 3 :"+Thread.currentThread().name)
            })

        }
    }

    //子线程
    private val mRunnableTwo = Runnable {
        run {

            Looper.prepare()

            threadHandler =object :Handler(Looper.myLooper()){
                override fun handleMessage(msg: Message) {
                    super.handleMessage(msg)
                    println("Current Thread-name 5 :"+Thread.currentThread().name)
                }
            }

            Looper.loop()

        }
    }

    //子线程
    private val mRunnableThree = Runnable {
        run {
            threadHandler.sendEmptyMessage(0)
            threadHandler.post(Runnable {
                println("Current Thread-name 4 :"+Thread.currentThread().name)
            })

        }
    }



}
