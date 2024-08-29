package com.devalb.wellbing2.util;

import org.springframework.stereotype.Service;

@Service
public class FormatoTexto {

    public String formatoTitulo(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        // Eliminar espacios
        String trimmed = input.replaceAll("\\s+", "");

        // Convertir a minúsculas
        String lowerCase = trimmed.toLowerCase();

        // Convertir la primera letra a mayúscula
        String formatted = Character.toUpperCase(lowerCase.charAt(0)) + lowerCase.substring(1);

        return formatted;
    }

    public String soloNumeros(String input) {

        // Reemplazar todo lo que no sea un dígito con una cadena vacía
        return input.replaceAll("[^\\d]", "");
    }

    public String primeraPalabra(String input) {

        String[] palabras = input.split("\\s+");

        return palabras.length > 0 ? palabras[0] : "";
    }

    public String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || nombreArchivo.isEmpty()) {
            return "";
        }

        int indiceUltimoPunto = nombreArchivo.lastIndexOf('.');
        if (indiceUltimoPunto > 0 && indiceUltimoPunto < nombreArchivo.length() - 1) {
            return nombreArchivo.substring(indiceUltimoPunto);
        } else {
            return "";
        }
    }
}
