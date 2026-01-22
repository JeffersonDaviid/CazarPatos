package com.chileno.jefferson.cazarpatos

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.util.Pair
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import java.io.IOException
import java.security.GeneralSecurityException

class LoginActivity : AppCompatActivity() {
    // Declaraciones de tus variables (sin cambios)
    lateinit var manejadorArchivo: FileHandler
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var buttonLogin: Button
    lateinit var buttonNewUser: Button
    lateinit var mediaPlayer: MediaPlayer
    lateinit var checkBoxRecordarme: CheckBox
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicialización de variables
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)
        auth = Firebase.auth

        manejadorArchivo = SharedPreferencesManager(this)
        LeerDatosDePreferencias()

        // Eventos clic
        buttonLogin.setOnClickListener {
            // 1. Validar que los campos no estén vacíos
            if (!validateRequiredData()) {
                return@setOnClickListener
            }

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // 2. Intentar autenticar al usuario con Firebase
            AutenticarUsuario(email, password)
        }

        buttonNewUser.setOnClickListener {
            // Lógica para registrar un nuevo usuario si es necesario
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (validateRequiredData()) {
                SignUpNewUser(email, password)
            }
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
    }


    fun AutenticarUsuario(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // --- NAVEGACIÓN CORRECTA ---
                    Log.d(EXTRA_LOGIN, "signInWithEmail:success")

                    // MENSAJE DE LOGIN CORRECTO AÑADIDO
                    Toast.makeText(
                        baseContext,
                        "Autenticación correcta. ¡Bienvenido!",
                        Toast.LENGTH_SHORT
                    ).show()

                    GuardarDatosEnPreferencias()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(EXTRA_LOGIN, auth.currentUser!!.email)
                    startActivity(intent)
                    finish()

                } else {
                    // --- FALLO EN EL LOGIN - LÓGICA DE ERRORES MEJORADA ---
                    Log.w(EXTRA_LOGIN, "signInWithEmail:failure", task.exception)

                    // Analizamos el tipo de excepción para dar un mensaje específico
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "El usuario no existe. Por favor, regístrese."
                        is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas. Revise su email o contraseña."
                        is FirebaseNetworkException -> "No hay conexión a internet. Por favor, verifique su red."
                        else -> "Error de autenticación: ${task.exception?.message}" // Mensaje genérico para otros errores
                    }

                    Toast.makeText(baseContext, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun validateRequiredData(): Boolean {
        val email = editTextEmail.text.toString()
        val password = editTextPassword.text.toString()
        if (email.isEmpty()) {
            editTextEmail.error = getString(R.string.error_email_required)
            editTextEmail.requestFocus()
            return false
        }
        if (password.isEmpty()) {
            editTextPassword.error = getString(R.string.error_password_required)
            editTextPassword.requestFocus()
            return false
        }
        if (password.length < 6) { // Firebase requiere mínimo 6 caracteres para la contraseña
            editTextPassword.error = "La contraseña debe tener al menos 6 caracteres."
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause()
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start()
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }

    private fun LeerDatosDePreferencias() {
        val listadoLeido = manejadorArchivo.readInformation()
        if (listadoLeido.first?.isNotEmpty() == true) {
            checkBoxRecordarme.isChecked = true
            editTextEmail.setText(listadoLeido.first)
            editTextPassword.setText(listadoLeido.second)
        } else {
            checkBoxRecordarme.isChecked = false
        }
    }

    private fun GuardarDatosEnPreferencias() {
        val email = editTextEmail.text.toString()
        val clave = editTextPassword.text.toString()
        val listadoAGrabar: Pair<String, String>

        if (checkBoxRecordarme.isChecked) {
            listadoAGrabar = Pair(email, clave)
        } else {
            listadoAGrabar = Pair("", "")
        }

        try {
            val spManager = SharedPreferencesManager(this)
            spManager.saveInformation(listadoAGrabar)
            val encryptedManager = EncriptedSharedPreferencesManager(this)
            encryptedManager.saveInformation(listadoAGrabar)
            val internalManager = FileInternalManager(this)
            internalManager.saveInformation(listadoAGrabar)
            val externalManager = FileExternalManager(this)
            externalManager.saveInformation(listadoAGrabar)
        } catch (e: GeneralSecurityException) {
            e.printStackTrace()
            Log.e("LoginActivity", "Error de seguridad al guardar las preferencias.")
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("LoginActivity", "Error de I/O al guardar las preferencias.")
        }
    }

    companion object {
        const val EXTRA_LOGIN = "EXTRA_LOGIN"
    }

    fun SignUpNewUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(EXTRA_LOGIN, "createUserWithEmail:success")
                    Toast.makeText(
                        baseContext, "Nuevo usuario registrado. Ahora inicia sesión.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Log.w(EXTRA_LOGIN, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "El registro falló: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
