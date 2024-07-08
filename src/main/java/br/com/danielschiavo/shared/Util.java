package br.com.danielschiavo.shared;

import java.time.Instant;
import java.util.UUID;

public class Util {

    public static String gerarUUID12Digitos() {
        String string = UUID.randomUUID().toString();
        int divisao = string.length() / 3;
        long timestamp = Instant.now().toEpochMilli();
        String substring = string.substring(0, divisao);
        return substring + timestamp;
    }
}
