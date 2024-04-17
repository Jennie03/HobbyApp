package com.ubaya.hobbyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ubaya.hobbyapp.R
import com.ubaya.hobbyapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}