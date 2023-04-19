package com.example.myprofilelayouts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


class AuthActivity : AppCompatActivity() {
    private val incorrectEntry: String = "Incorrect entry"
    private lateinit var fieldMail: TextInputLayout
    private lateinit var textMail: TextInputEditText
    private lateinit var fieldPassword: TextInputLayout
    private lateinit var textPassword: TextInputEditText
    private val TAG = "AuthActivity"
    private var rememberMe = false
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        textMail = findViewById<View>(R.id.email_text) as TextInputEditText
        fieldMail = findViewById<View>(R.id.email) as TextInputLayout
        textPassword = findViewById<View>(R.id.password_text) as TextInputEditText
        fieldPassword = findViewById<View>(R.id.password) as TextInputLayout

        userPreferences = UserPreferences(this)

        userPreferences.mail.asLiveData().observe(this, Observer {
            fieldMail.helperText = it
        })
    }

    fun onCheckboxClicked(view: View) {
        rememberMe = !rememberMe
    }

    fun register(view: View) {
        var mail: String = mailExtraction()
        var password: String = passwordExtraction()
        if (mail == incorrectEntry) fieldMail.error = incorrectEntry
        else fieldMail.error = null
        if (password == incorrectEntry) fieldPassword.error = incorrectEntry
        else fieldPassword.error = null
        if (rememberMe) {
            val mail = textMail.text.toString().trim()
            lifecycleScope.launch {
                userPreferences.saveBookmark(mail)
            }
        }
        if (mail != incorrectEntry && password != incorrectEntry) {
            createIntentMainActivity(mail, password)
        }
    }

    private fun createIntentMainActivity(mail: String, password: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("mail", mail)
        //intent.putExtra("password", password)
        startActivity(intent)
        overridePendingTransition(R.anim.right, R.anim.left);
    }


    private fun passwordExtraction(): String {
        var password: String = textPassword.text.toString()
        return if (isPasswordValid(password)) password
        else incorrectEntry
    }

    private fun mailExtraction(): String {
        var mail: String = textMail.text.toString()
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


class UserPreferences(
    context: Context
) {
    private val applicationContext = context.applicationContext
    private val dataStore: DataStore<Preferences> = applicationContext.createDataStore(
        name = "app_preferences"
    )

    val mail: Flow<String?>
        get() = dataStore.data.map { preferences ->
            preferences[KEY_MAIL]
        }

    suspend fun saveBookmark(mail: String) {
        dataStore.edit { preferences ->
            preferences[KEY_MAIL] = mail
        }
    }


    companion object {
        val KEY_MAIL = preferencesKey<String>("key_mail")
    }
}