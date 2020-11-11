package com.example.camelproxy;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GenerateToken {
    private final SecureRandom secureRandom = new SecureRandom();
    private final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    private Map<String, List<String>> tokenMap = new HashMap<>();
    private static GenerateToken instance;

    private GenerateToken(){}

    public static GenerateToken getInstance(){
        if (instance == null)
            instance = new GenerateToken();

        return instance;
    }

    public String generateNewToken(){
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public Map<String, List<String>> getTokenMap() {
        return tokenMap;
    }
}
