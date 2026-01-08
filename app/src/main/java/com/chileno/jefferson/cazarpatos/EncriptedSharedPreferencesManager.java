package com.chileno.jefferson.cazarpatos;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Pair;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncriptedSharedPreferencesManager implements FileHandler {

    // Constantes para las claves de SharedPreferences
    private static final String LOGIN_KEY = "LOGIN_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";
    private static final String FILE_NAME = "secret_shared_prefs";

    private SharedPreferences sharedPreferences;

    // El constructor ahora puede lanzar excepciones durante la creación de la MasterKey
    public EncriptedSharedPreferencesManager(Activity actividad) throws GeneralSecurityException, IOException {
        // 1. Construir la MasterKey
        MasterKey masterKey = new MasterKey.Builder(actividad)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        // 2. Crear la instancia de EncryptedSharedPreferences
        this.sharedPreferences = EncryptedSharedPreferences.create(
                actividad, // context
                FILE_NAME, // filename
                masterKey, // masterKey
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    @Override
    public void saveInformation(Pair<String, String> datosAGrabar) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGIN_KEY, datosAGrabar.first);
        editor.putString(PASSWORD_KEY, datosAGrabar.second);
        editor.apply();
    }

    @Override
    public Pair<String, String> readInformation() {
        String email = sharedPreferences.getString(LOGIN_KEY, "");
        String clave = sharedPreferences.getString(PASSWORD_KEY, "");
        // En Java, se crea un Pair con 'new'
        return new Pair<>(email, clave);
    }




}
