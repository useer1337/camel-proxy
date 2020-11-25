package ru.hostco.camel.proxy.token;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 *  Класс который генерирует токены и содержит map с patientId
 */
public class GenerateToken {
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private Map<String, Map<String, String>> tokenMap = new HashMap<>();
    private static GenerateToken instance;

    private GenerateToken(){}

    public static GenerateToken getInstance(){
        if (instance == null)
            instance = new GenerateToken();

        return instance;
    }


    /**
     * Метод генерирует токены
     *
     * @return сгенерированный токен
     */
    public String generateNewToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public Map<String, Map<String, String>> getTokenMap() {
        return tokenMap;
    }
}
