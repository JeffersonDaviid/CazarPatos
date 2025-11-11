package com.chileno.jefferson.cazarpatos

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var emailInputLayout: TextInputLayout
    private lateinit var passwordInputLayout: TextInputLayout
    private lateinit var loginButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        emailInputLayout = findViewById(R.id.emailInputLayout)
        passwordInputLayout = findViewById(R.id.passwordInputLayout)
        loginButton = findViewById(R.id.loginButton)

        // Set up login button click listener
        loginButton.setOnClickListener {
            validateAndLogin()
        }
    }

    private fun validateAndLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString()

        var isValid = true

        // Validate email
        if (!LoginValidator.isValidEmail(email)) {
            emailInputLayout.error = getString(R.string.error_invalid_email)
            isValid = false
        } else {
            emailInputLayout.error = null
        }

        // Validate password (minimum 8 characters)
        if (!LoginValidator.isValidPassword(password)) {
            passwordInputLayout.error = getString(R.string.error_password_length)
            isValid = false
        } else {
            passwordInputLayout.error = null
        }

        // If all validations pass, navigate to MainActivity
        if (isValid) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close login activity so user can't go back
        }
    }
}
