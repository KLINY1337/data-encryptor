package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.SecretKey;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Setter
@Getter
@Service
@PropertySource("application.properties")
public class SecretKeyService {
    private final String generationMethod;
    private final int keyBitSize;

    public SecretKeyService(@Value("${secret.key.generation.method}") String generationMethod,
                            @Value("${secret.key.bit.size}") int keyBitSize) {
        this.generationMethod = generationMethod;
        this.keyBitSize = keyBitSize;
    }

    public SecretKey getSecretKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(generationMethod);
            SecureRandom secureRandom = new SecureRandom();
            keyGenerator.init(keyBitSize, secureRandom);

            //char[] base64EncodedSecretKey = Base64.getEncoder().encodeToString(keyGenerator.generateKey().getEncoded()).toCharArray();

            //return new KeyStore.PasswordProtection(base64EncodedSecretKey);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
