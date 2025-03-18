package com.classlocator.nitrr.interfaces;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class Confidential {

    private Confidential() {
        throw new IllegalStateException("Utility class");
    }

    public static String getSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        SecretKey sKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(sKey.getEncoded());
    }
}
