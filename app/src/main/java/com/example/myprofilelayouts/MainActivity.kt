package com.example.myprofilelayouts

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofilelayouts.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val arguments: Bundle? = intent.extras
        binding.name.text = extractName(arguments)
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