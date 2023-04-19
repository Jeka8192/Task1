package com.example.myprofilelayouts

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var name = findViewById<TextView>(R.id.name)
        val arguments: Bundle? = intent.extras
        name.text = extractName(arguments)
    }

    private fun extractName(arguments: Bundle?): String {
        var userName = ""
        if (arguments != null) {
            val mail = arguments.getString("mail")
            val substringMail: String? = mail?.substringBefore('@')
            val listMail: List<String>? = substringMail?.split(".")
            if (listMail != null) {
                for (str: String in listMail) {
                    userName += str.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                        else it.toString()
                    }
                    userName += " "
                }
            }
        }
        return userName
    }

}