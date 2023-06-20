package com.example.wattercount.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.wattercount.models.Article
import com.example.wattercount.R

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val article = intent.getSerializableExtra("article") as? Article

        if (article != null) {
            val image = findViewById<ImageView>(R.id.imageView)
            val title = findViewById<TextView>(R.id.titleTextView)
            val text = findViewById<TextView>(R.id.textTextView)

            image.setImageResource(article.image ?: 0)
            title.text = article.title
            text.text = article.text
        } else {
            finish()
        }
        val closeButton = findViewById<Button>(R.id.closeButton)
        closeButton.setOnClickListener {
            finish()
        }
    }
}
