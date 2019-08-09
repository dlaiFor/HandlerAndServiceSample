package com.example.messenger

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Toast

const val MSG_SAY_HELLO = 1
const val MSG_SAY_FEEDBACK = 2

class MessengerService : Service() {
    /**
     * Target we publish for clients to send messages to IncomingHandler.
     * 用于接收client发来的消息Messenger
     */
    private lateinit var mMessenger: Messenger

    /**
     * Handler of incoming messages from clients.
     */
    inner class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext

    ) : Handler() {
        override fun handleMessage(msgFromToClient: Message) {
            when (msgFromToClient.what) {
                MSG_SAY_HELLO ->
                    Log.d("MessengerService","客户端发送一条消息")
                MSG_SAY_FEEDBACK ->{
                    val replyMessage = Message.obtain(null, 100)
                    val bundle = Bundle()
                    Log.d("MessengerService",msgFromToClient.data.getString("replyTo"))
                    bundle.putString("reply", "ok 现在来测试能不能听见我的回复")
                    replyMessage.data = bundle
                    msgFromToClient.replyTo.send(replyMessage)

                }




                else -> super.handleMessage(msgFromToClient)


            }
        }
    }

    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    override fun onBind(p0: Intent?): IBinder? {
        Toast.makeText(applicationContext, "binding", Toast.LENGTH_SHORT).show()
        mMessenger = Messenger(IncomingHandler(this))
        return mMessenger.binder
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("MessengerService","我可能要关闭了")
    }


}