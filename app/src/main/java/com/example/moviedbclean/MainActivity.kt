package com.example.moviedbclean

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviedbclean.presentation.ui.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.constraintMain, HomeFragment.newInstance())
            .commit()
    }
}
