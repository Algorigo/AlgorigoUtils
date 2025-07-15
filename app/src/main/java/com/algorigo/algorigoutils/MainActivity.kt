package com.algorigo.algorigoutils

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algorigo.algorigoutils.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.dateTimeUtilBtn.setOnClickListener {
            val intent = Intent(this, DateTimeUtilActivity::class.java)
            startActivity(intent)
        }
        binding.loginValidationBtn.setOnClickListener {
            val intent = Intent(this, LoginValidationActivity::class.java)
            startActivity(intent)
        }
        binding.wifiBtn.setOnClickListener {
            val intent = Intent(this, WifiActivity::class.java)
            startActivity(intent)
        }
    }
}
