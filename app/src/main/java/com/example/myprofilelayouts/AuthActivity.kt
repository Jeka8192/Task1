package com.example.myprofilelayouts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myprofilelayouts.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private val incorrectEntry: String = "Incorrect entry"
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun onCheckboxClicked(view: View) {
    }

    fun register(view: View) {
        val mail: String = mailExtraction()
        val password: String = passwordExtraction()
        if (mail == incorrectEntry) binding.email.error = incorrectEntry
        else binding.email.error = null
        if (password == incorrectEntry) binding.password.error = incorrectEntry
        else binding.password.error = null
        if (mail != incorrectEntry && password != incorrectEntry) {
            createIntentMainActivity(mail)
        }
    }

    private fun createIntentMainActivity(mail: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("mail", mail)
        startActivity(intent)
        overridePendingTransition(R.anim.right, R.anim.left)
    }


    private fun passwordExtraction(): String {
        val password: String = binding.passwordText.text.toString()
        return if (isPasswordValid(password)) password
        else incorrectEntry
    }

    private fun mailExtraction(): String {
        val mail: String = binding.emailText.text.toString()
        return if (isEmailValid(mail)) mail
        else incorrectEntry
    }

    private fun isEmailValid(mail: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }

    private fun isPasswordValid(password: String): Boolean {
        val reg: Regex = "[\ba-zA-Z]+".toRegex()
        return if (password.length < 8 || password.length > 48) false
        else password.matches(reg)
    }
}