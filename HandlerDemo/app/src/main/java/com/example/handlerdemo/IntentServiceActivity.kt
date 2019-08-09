package com.example.handlerdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.intentservice.QueueService
import kotlinx.android.synthetic.main.activity_servicedemo.*

class IntentServiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_servicedemo)


        start.setOnClickListener {
            val serviceClass = QueueService::class.java

            val intent = Intent(applicationContext, serviceClass)
            startService(intent)
        }

    }
}