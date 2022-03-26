package com.example.twitchclient.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.twitchclient.databinding.ActivityMainBinding
import com.example.twitchclient.ui.auth.AuthorizationActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnToAuth.apply {
            setOnClickListener {
                startActivity(
                    Intent(this@MainActivity, AuthorizationActivity::class.java)
                )
            }
        }
    }
}