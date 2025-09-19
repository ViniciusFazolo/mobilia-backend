package com.example.mobilia.utils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.multipart.MultipartFile;

public class Utils {

    public static String extraiCampoDoErro(String frase) {
        // Regex para capturar qualquer coisa após o ponto no final do campo
        Pattern pattern = Pattern.compile("for key '.*?\\.(\\w+)'");
        Matcher matcher = pattern.matcher(frase);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static byte[] multipartToBytes(MultipartFile file) {
        try {
            return file != null ? file.getBytes() : null;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao converter MultipartFile para byte[]", e);
        }
    }
}
