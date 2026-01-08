package com.chileno.jefferson.cazarpatos;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

public class SharedPreferencesManager implements FileHandler {

     private static final String LOGIN_KEY = "LOGIN_KEY";
    private static final String PASSWORD_KEY = "PASSWORD_KEY";

    private Activity actividad;

     public SharedPreferencesManager(Activity actividad) {
        this.actividad = actividad;
    }

    @Override
    public void saveInformation(Pair<String, String> datosAGrabar) {
         SharedPreferences sharedPref = actividad.getPreferences(Context.MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPref.edit();

        // Guardamos el email y la contraseña
        editor.putString(LOGIN_KEY, datosAGrabar.first);
        editor.putString(PASSWORD_KEY, datosAGrabar.second);

        // Aplicamos los cambios de forma asíncrona
        editor.apply();
    }

    @Override
    public Pair<String, String> readInformation() {
        // Obtenemos una instancia de SharedPreferences
        SharedPreferences sharedPref =actividad.getPreferences(Context.MODE_PRIVATE);

        // Leemos el email, si no existe, devuelve un string vacío ""
        String email = sharedPref.getString(LOGIN_KEY, "");
        // Leemos la contraseña, si no existe, devuelve un string vacío ""
        String clave = sharedPref.getString(PASSWORD_KEY, "");

        // Devolvemos los datos en un objeto Pair
        return new Pair<>(email, clave);
    }
}

