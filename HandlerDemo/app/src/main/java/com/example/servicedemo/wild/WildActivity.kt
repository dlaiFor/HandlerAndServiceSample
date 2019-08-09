package com.example.servicedemo.wild

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.handlerdemo.R
import com.example.servicedemo.service.WildService
import kotlinx.android.synthetic.main.activity_servicedemo.*

/**
 * startService
 */
class WildActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicedemo)

        val serviceClass = WildService::class.java

        val intent = Intent(applicationContext, serviceClass)


        start.setOnClickListener {
            if (!isServiceRunning(serviceClass)) {

                intent.putExtra("flag","start")
                startService(intent)

            } else {
                Toast.makeText(this," already running ",Toast.LENGTH_SHORT).show()
            }

        }

        stop.setOnClickListener {
            if (isServiceRunning(serviceClass)) {
                stopService(intent)
            } else {
                Toast.makeText(this," already stop ",Toast.LENGTH_SHORT).show()
            }
        }

        is_running.setOnClickListener {
            if (isServiceRunning(serviceClass)) {
                Toast.makeText(this," running ",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this," stopped ",Toast.LENGTH_SHORT).show()
            }

        }


    }

    // Custom method to determine whether a service is running
    @Suppress("DEPRECATION")
    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }

}