package com.chileno.jefferson.cazarpatos;

import android.app.Activity;
import android.content.Context;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileInternalManager implements FileHandler {

    private final Activity actividad;
    private static final String FILE_NAME = "fichero.txt";

    // Constructor para recibir la Activity
    public FileInternalManager(Activity actividad) {
        this.actividad = actividad;
    }

    @Override
    public void saveInformation(Pair<String, String> datosAGrabar) {
        // Combina el email y la contraseña, separados por un salto de línea
        String texto = datosAGrabar.first + System.getProperty("line.separator") + datosAGrabar.second;

        // try-with-resources para asegurar que el stream se cierre automáticamente
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(actividad.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)))) {
            bufferedWriter.write(texto);
        } catch (Exception e) {
            e.printStackTrace(); // Es buena práctica registrar el error
        }
    }

    @Override
    public Pair<String, String> readInformation() {
        // try-with-resources para asegurar que el stream se cierre automáticamente
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(actividad.openFileInput(FILE_NAME)))) {

            String email = bufferedReader.readLine(); // Lee la primera línea (email)
            String clave = bufferedReader.readLine(); // Lee la segunda línea (contraseña)

            // Si los datos no son nulos, los devuelve. Si no, devuelve un par vacío.
            if (email != null && clave != null) {
                return new Pair<>(email, clave);
            } else {
                return new Pair<>("", "");
            }

        } catch (Exception e) {
            // Si ocurre una excepción (ej. el archivo no existe), devuelve un par vacío
            e.printStackTrace();
            return new Pair<>("", "");
        }
    }
}
