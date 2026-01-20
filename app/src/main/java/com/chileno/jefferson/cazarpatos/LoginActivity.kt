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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // enableEdgeToEdge() // A menudo es mejor gestionarlo manualmente
        setContentView(R.layout.activity_login)

        // Inicialización de variables (sin cambios)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonNewUser = findViewById(R.id.buttonNewUser)
        checkBoxRecordarme = findViewById(R.id.checkBoxRecordarme)

        // Inicializa solo un manejador de archivos por defecto
        manejadorArchivo = SharedPreferencesManager(this)
        LeerDatosDePreferencias()

        // Eventos clic
        buttonLogin.setOnClickListener {
            // Validaciones de datos requeridos y formatos
            if (!validateRequiredData()) {
                return@setOnClickListener
            }

            // Si pasa la validación, guardamos los datos
            GuardarDatosEnPreferencias()

            // Navegar a MainActivity
            val email = editTextEmail.text.toString()
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EXTRA_LOGIN, email) // EXTRA_LOGIN debe estar definido en alguna parte
            startActivity(intent)
            finish() // Cierra LoginActivity para que el usuario no pueda volver con el botón "atrás"
        }

        buttonNewUser.setOnClickListener {
            // Lógica para registrar un nuevo usuario si es necesario
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.title_screen)
        mediaPlayer.isLooping = true // Opcional: para que la música se repita
        mediaPlayer.start()
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
        if (password.length < 3) { // El mínimo debería ser más alto en una app real
            editTextPassword.error = getString(R.string.error_password_min_length)
            editTextPassword.requestFocus()
            return false
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        mediaPlayer.pause() // Pausa la música si la actividad pasa a segundo plano
    }

    override fun onResume() {
        super.onResume()
        mediaPlayer.start() // Reanuda la música cuando la actividad vuelve al frente
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        super.onDestroy()
    }

    private fun LeerDatosDePreferencias() {
        val listadoLeido = manejadorArchivo.readInformation()
        // Comprobar que el dato leído no sea nulo ni vacío
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

        // Bloque try-catch para manejar los posibles errores al guardar archivos
        try {
            // Guardar con SharedPreferences (texto plano)
            val spManager = SharedPreferencesManager(this)
            spManager.saveInformation(listadoAGrabar)

            // Guardar con EncryptedSharedPreferences (encriptado)
            val encryptedManager = EncriptedSharedPreferencesManager(this)
            encryptedManager.saveInformation(listadoAGrabar)

            // Guardar en almacenamiento interno
            val internalManager = FileInternalManager(this)
            internalManager.saveInformation(listadoAGrabar)

            // Guardar en almacenamiento externo
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

    // Asegúrate de que esta constante esté definida
    companion object {
        const val EXTRA_LOGIN = "EXTRA_LOGIN"
    }
}
