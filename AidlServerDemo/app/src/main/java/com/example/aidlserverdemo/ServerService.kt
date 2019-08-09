package com.example.aidlserverdemo

import android.app.Service
import android.content.Intent
import android.os.*
import android.text.TextUtils
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList


const val REPORT_MSG = 1
class ServerService : Service() {

    private val mSiteList = CopyOnWriteArrayList<WebSite>()

    val mCallbacks = RemoteCallbackList<IServerCallBack>()

    private var mValue = 0


    override fun onCreate() {
        super.onCreate()
        mSiteList.add(WebSite(0,"google","www.google.com"))
        mSiteList.add(WebSite(1,"baidu","www.baidu.com"))
        mSiteList.add(WebSite(2,"sina","www.sina.com"))

        mHandler.sendEmptyMessage(REPORT_MSG)
    }


    override fun onBind(intent: Intent): IBinder? {

        if (IRemoteService::class.java.name == intent.action) {
            Log.d("ServerService","callBackClient")
            return callBackClient
        }
        if (IBridge::class.java.name == intent.action) {
            Log.d("ServerService","toServerBinder")
            return toServerBinder
        }
        Log.d("ServerService","null")
        return null
    }

    //client request access binder
    private val toServerBinder = object  : IBridge.Stub(){

        override fun getSingleWebSite(name: String?): WebSite {

            for (it in mSiteList){
                 if (TextUtils.equals(it.name,name)) return it
            }
            return WebSite(-1,null,null)
        }

        override fun getWebSites(): MutableList<WebSite> {
            return mSiteList
        }

        override fun getGoogleUrl(id: Int): String? {
            Log.d("ServerService","getGoogleUrl")
            return mSiteList[0].url
        }

        override fun addWebSite(site: WebSite?): Int {
            mSiteList.add(site)
            return 1
        }

    }

    /**
     * The IRemoteInterface is defined through IDL
     *  server push access binder
     */
    private val callBackClient = object : IRemoteService.Stub() {
        override fun registerCallback(callBack: IServerCallBack?) {
            if (callBack != null) mCallbacks.register(callBack)
        }

        override fun unregisterCallback(callBack: IServerCallBack?) {
            if (callBack != null) mCallbacks.unregister(callBack)
        }

    }

    /**
     * Our Handler used to execute operations on the main thread.  This is used
     * to schedule increments of our value.
     */
    private val mHandler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                // It is time to bump the value!
                REPORT_MSG -> {
                    // Up it goes.
                    val value = ++mValue
                    // Broadcast to all clients the new value.
                    val N = mCallbacks.beginBroadcast()
                    for (i in 0 until N) {
                        try {
                            mCallbacks.getBroadcastItem(i).valueChanged(value)
                        } catch (e: RemoteException) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }

                    }
                    mCallbacks.finishBroadcast()
                    // Repeat every 1 second.
                    sendMessageDelayed(obtainMessage(REPORT_MSG), 1 * 1000)
                }
                else -> super.handleMessage(msg)
            }
        }
    }
}
