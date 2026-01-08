package com.chileno.jefferson.cazarpatos;

import android.app.Activity;
import android.os.Environment;
import android.util.Pair;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileExternalManager implements FileHandler {

    private final Activity actividad;
    // Es una buena práctica definir el nombre del archivo como una constante
    private static final String SHAREDINFO_FILENAME = "user_external_data.txt";

    // Constructor para inicializar la Activity
    public FileExternalManager(Activity actividad) {
        this.actividad = actividad;
    }

    // Comprueba si el almacenamiento externo está disponible para escritura
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    // Comprueba si el almacenamiento externo está disponible para lectura
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @Override
    public void saveInformation(Pair<String, String> datosAGrabar) {
        if (!isExternalStorageWritable()) {
            // Si no se puede escribir, no hacemos nada o registramos un error
            return;
        }

        File file = new File(actividad.getExternalFilesDir(null), SHAREDINFO_FILENAME);

        // Usamos try-with-resources para garantizar que el stream se cierre
        try (FileOutputStream fos = new FileOutputStream(file);
             OutputStreamWriter osw = new OutputStreamWriter(fos)) {

            osw.write(datosAGrabar.first);
            osw.write(System.getProperty("line.separator")); // Salto de línea dependiente del sistema
            osw.write(datosAGrabar.second);

        } catch (IOException e) {
            e.printStackTrace(); // Registrar el error
        }
    }

    @Override
    public Pair<String, String> readInformation() {
        if (!isExternalStorageReadable()) {
            return new Pair<>("", "");
        }

        File file = new File(actividad.getExternalFilesDir(null), SHAREDINFO_FILENAME);

        // Comprobamos si el archivo realmente existe antes de intentar leerlo
        if (!file.exists()) {
            return new Pair<>("", "");
        }

        // Usamos try-with-resources para la lectura segura
        try (FileInputStream fis = new FileInputStream(file);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader bufferedReader = new BufferedReader(isr)) {

            String email = bufferedReader.readLine();
            String clave = bufferedReader.readLine();

            if (email != null && clave != null) {
                return new Pair<>(email, clave);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Registrar el error
        }

        // Si algo falla (archivo vacío, error de lectura, etc.), devolvemos un par vacío
        return new Pair<>("", "");
    }
}
