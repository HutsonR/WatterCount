package com.example.wattercount.Activity

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.wattercount.R

class HistoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val closeButton = findViewById<ImageButton>(R.id.close_history_activity)
        closeButton.setOnClickListener {
            finish()
        }
    }
}
