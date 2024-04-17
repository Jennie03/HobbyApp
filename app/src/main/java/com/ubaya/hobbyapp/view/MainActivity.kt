package com.ubaya.hobbyapp.view

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.ubaya.hobbyapp.R
import com.ubaya.hobbyapp.databinding.ActivityMainBinding
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var  binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    companion object {
        fun load_pict(view: View, photo: String, imageView: ImageView) {
            val picasso = Picasso.Builder(view.context)
            picasso.listener { picasso, uri, exception ->
                exception.printStackTrace()
            }
            picasso.build().load(photo)
                .into(imageView, object : Callback {
                    override fun onSuccess() {
                        imageView.visibility = View.VISIBLE
                    }

                    override fun onError(e: Exception?) {
                        Log.d("picasso error", e.toString())
                    }
                })
        }
    }
}