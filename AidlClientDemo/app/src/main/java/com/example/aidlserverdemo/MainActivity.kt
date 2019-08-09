package com.example.aidlserverdemo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

private const val BUMP_MSG = 1

class MainActivity : AppCompatActivity() {

    /** The primary interface we will be calling on the service.  */
    private var mService: IBridge? = null

    private var mCallBackService: IRemoteService? = null

    private var mBounds = false

    private lateinit var handler: InternalHandler


    private val connBridge = object :ServiceConnection {


        override fun onServiceConnected(className : ComponentName?, service: IBinder?) {

            mService  = IBridge.Stub.asInterface(service)

            mBounds =true

        }

        override fun onServiceDisconnected(className : ComponentName?) {

            mBounds =false

            mService=null
        }
    }


    private val connCallBack = object :ServiceConnection {


        override fun onServiceConnected(className : ComponentName?, service: IBinder?) {

            mCallBackService  = IRemoteService.Stub.asInterface(service)


            // We want to monitor the service for as long as we are
            // connected to it. 把回调的引用传递过去了 这样server就可以再处理之后调用了 也就是双向通信
            try {
                mCallBackService?.registerCallback(mCallback)
            } catch (e: RemoteException) {
                // In this case the service has crashed before we could even
                // do anything with it; we can count on soon being
                // disconnected (and then reconnected if it can be restarted)
                // so there is no need to do anything here.
            }


            Toast.makeText(this@MainActivity, " server push client connect success", Toast.LENGTH_SHORT).show()

        }

        override fun onServiceDisconnected(className : ComponentName?) {

            mService=null
        }
    }

    override fun onStart() {

        super.onStart()

        val intent = Intent()
        /**
         *
         *    <action android:name="com.example.aidlserverdemo.IRemoteService" />
              <action android:name="com.example.aidlserverdemo.ServerService"/>

               connCallBack
               connBridge

              这里通过改变action和绑定的链接 来调用 接口 还是 回调
         */


        intent.action = "com.example.aidlserverdemo.IRemoteService"

        intent.setPackage("com.example.aidlserverdemo")

       var b  =bindService(intent, connCallBack, Context.BIND_AUTO_CREATE)

        Log.d("Client Log :", b.toString())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = InternalHandler(data)

        all.setOnClickListener {

           var webSites :ArrayList<WebSite> = mService?.webSites as ArrayList<WebSite>

           var buffer = StringBuffer()

            for (it in webSites){
                buffer.append(it.toString())
            }

            data.text =buffer.toString()
        }

        google_url.setOnClickListener {
            data.text =  mService?.getGoogleUrl(0)
        }

        web_site.setOnClickListener {
           mService?.getSingleWebSite("google").let {
               webSite -> data.text =webSite.toString()
           }
        }

    }

    /**
     * This implementation is used to receive callbacks from the remote
     * service.
     */
    private val mCallback = object : IServerCallBack.Stub() {
        /**
         * This is called by the remote service regularly to tell us about
         * new values.  Note that IPC calls are dispatched through a thread
         * pool running in each process, so the code executing here will
         * NOT be running in our main thread like most other things -- so,
         * to update the UI, we need to use a Handler to hop over there.
         */
        override fun valueChanged(value: Int) {
            handler.sendMessage(handler.obtainMessage(BUMP_MSG, value, 0))
        }
    }

    private class InternalHandler(
        textView: TextView,
        private val weakTextView: WeakReference<TextView> = WeakReference(textView)
    ) : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                BUMP_MSG -> weakTextView.get()?.text = "Received from service: ${msg.arg1}"
                else -> super.handleMessage(msg)
            }
        }
    }

}
