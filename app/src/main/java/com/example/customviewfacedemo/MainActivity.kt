package com.example.customviewfacedemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.customviewfacedemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.happyButton.setOnClickListener {
            binding.emojiFaceView.happinessState = EmojiFaceView.HAPPY
        }

        binding.sadButton.setOnClickListener {
            binding.emojiFaceView.happinessState = EmojiFaceView.SAD
        }

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}