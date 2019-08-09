package com.example.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*




const val MSG_SAY_HELLO = 1
const val MSG_SAY_FEEDBACK = 2

class MainActivity : AppCompatActivity() {

    /** Messenger for communicating with the service.  */
    private var mService: Messenger? = null

    /** Messenger for reply  the service.  */
    private lateinit var replyMessenger: Messenger

    /** Flag indicating whether we have called bind on the service.  */
    private var bound: Boolean = false


    class GetReplyHandler : Handler() {
        private var TAG = "MainActivity"
        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                100 -> {
                    Log.d(TAG, msg.data.getString("reply"))
                }
            }
        }
    }


    /**
     * Class for interacting with the main interface of the service.
     */
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // This is called when the connection with the service has been
            // established, giving us the object we can use to
            // interact with the service.  We are communicating with the
            // service using a Messenger, so here we get a client-side
            // representation of that from the raw IBinder object.
            mService = Messenger(service)
            bound = true
            Log.d("client1","connect")
        }

        override fun onServiceDisconnected(className: ComponentName) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null
            bound = false
        }
    }

    fun sayHello() {
        if (!bound) return
        // Create and send a message to the service, using a supported 'what' value
        val msg: Message = Message.obtain(null, MSG_SAY_HELLO, 0, 0)
        try {
            mService?.send(msg)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.clientno1.R.layout.activity_main)

        replyMessenger = Messenger(GetReplyHandler())

        say.setOnClickListener {
            sayHello()
        }

        send.setOnClickListener {
            val msg = Message.obtain(null, MSG_SAY_FEEDBACK)
            val bundle = Bundle()
            bundle.putString("replyTo", "?????")
            msg.data = bundle
            msg.replyTo = replyMessenger
            mService?.send(msg)
        }
    }

    override fun onStart() {
        super.onStart()
        // Bind to the service
        val intent = Intent()
        intent.action = "com.example.messenger.MessengerService"
        val eintent = Intent(createExplicitFromImplicitIntent(applicationContext, intent))
        var result : Boolean =bindService(eintent, mConnection, Context.BIND_AUTO_CREATE)
            Log.d("client1", "result : $result")
    }

    override fun onStop() {
        super.onStop()
        // Unbind from the service
        if (bound) {
            unbindService(mConnection)
            bound = false
        }
    }

    private fun createExplicitFromImplicitIntent(context: Context, implicitIntent: Intent): Intent? {
        // Retrieve all services that can match the given intent
        val pm = context.packageManager
        val resolveInfo = pm.queryIntentServices(implicitIntent, 0)

        // Make sure only one match was found
        if (resolveInfo == null || resolveInfo.size != 1) {
            return null
        }

        // Get component info and create ComponentName
        val serviceInfo = resolveInfo[0]
        val packageName = serviceInfo.serviceInfo.packageName
        val className = serviceInfo.serviceInfo.name
        val component = ComponentName(packageName, className)

        // Create a new intent. Use the old one for extras and such reuse
        val explicitIntent = Intent(implicitIntent)

        // Set the component to be explicit
        explicitIntent.component = component

        return explicitIntent
    }
}
