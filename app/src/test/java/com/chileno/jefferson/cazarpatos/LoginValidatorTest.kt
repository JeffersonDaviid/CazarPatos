package com.chileno.jefferson.cazarpatos

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for LoginValidator
 */
class LoginValidatorTest {

    @Test
    fun isValidEmail_withValidEmail_returnsTrue() {
        assertTrue(LoginValidator.isValidEmail("test@example.com"))
        assertTrue(LoginValidator.isValidEmail("user.name@example.com"))
        assertTrue(LoginValidator.isValidEmail("user+tag@example.co.uk"))
    }

    @Test
    fun isValidEmail_withInvalidEmail_returnsFalse() {
        assertFalse(LoginValidator.isValidEmail(""))
        assertFalse(LoginValidator.isValidEmail("notanemail"))
        assertFalse(LoginValidator.isValidEmail("@example.com"))
        assertFalse(LoginValidator.isValidEmail("user@"))
        assertFalse(LoginValidator.isValidEmail("user @example.com"))
    }

    @Test
    fun isValidPassword_withValidPassword_returnsTrue() {
        assertTrue(LoginValidator.isValidPassword("12345678"))
        assertTrue(LoginValidator.isValidPassword("password"))
        assertTrue(LoginValidator.isValidPassword("abcdefgh"))
        assertTrue(LoginValidator.isValidPassword("a1b2c3d4e5"))
    }

    @Test
    fun isValidPassword_withShortPassword_returnsFalse() {
        assertFalse(LoginValidator.isValidPassword(""))
        assertFalse(LoginValidator.isValidPassword("1234567"))
        assertFalse(LoginValidator.isValidPassword("short"))
        assertFalse(LoginValidator.isValidPassword("abc"))
    }

    @Test
    fun isValidPassword_withExactly8Characters_returnsTrue() {
        assertTrue(LoginValidator.isValidPassword("exactly8"))
    }
}
