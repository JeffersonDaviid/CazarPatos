package com.chileno.jefferson.cazarpatos

import android.util.Patterns

object LoginValidator {
    
    fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
}
