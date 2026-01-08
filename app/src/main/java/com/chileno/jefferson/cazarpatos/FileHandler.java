package com.chileno.jefferson.cazarpatos;

import android.util.Pair;

public interface FileHandler {

    void saveInformation(Pair<String, String> datosAGrabar);

    Pair<String, String> readInformation();

}
