package com.devalb.wellbing2.util;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

@Service
public class CrearCodigo {

    private static final char[] MAYUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] MINUSCULAS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] NUMEROS = "1234567890".toCharArray();
    private static final char[] CARACTERES;

    static {
        CARACTERES = new char[MAYUSCULAS.length + MINUSCULAS.length + NUMEROS.length];
        System.arraycopy(MAYUSCULAS, 0, CARACTERES, 0, MAYUSCULAS.length);
        System.arraycopy(MINUSCULAS, 0, CARACTERES, MAYUSCULAS.length, MINUSCULAS.length);
        System.arraycopy(NUMEROS, 0, CARACTERES, MAYUSCULAS.length + MINUSCULAS.length, NUMEROS.length);
    }

    public String generarCodigo(List<String> codigos) {
        String codigo;
        do {
            codigo = generarCadenaAleatoria(7); // Tamaño 7 porque es 0-6 inclusive
        } while (codigos.contains(codigo));
        return codigo;
    }

    public String generarPass() {
        return generarCadenaAleatoria(10);
    }

    private String generarCadenaAleatoria(int longitud) {
        StringBuilder cadena = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int indexAleatorio = ThreadLocalRandom.current().nextInt(CARACTERES.length);
            cadena.append(CARACTERES[indexAleatorio]);
        }
        return cadena.toString();
    }
}
