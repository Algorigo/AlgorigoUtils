package com.algorigo.algorigoutils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dateTimeUtilBtn.setOnClickListener {
            val intent = Intent(this, DateTimeUtilActivity::class.java)
            startActivity(intent)
        }
        loginValidationBtn.setOnClickListener {
            val intent = Intent(this, LoginValidationActivity::class.java)
            startActivity(intent)
        }
        wifiBtn.setOnClickListener {
            val intent = Intent(this, WifiActivity::class.java)
            startActivity(intent)
        }
    }
}
