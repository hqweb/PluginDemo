package com.example.plugindemo.hook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.plugindemo.R

class StubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}