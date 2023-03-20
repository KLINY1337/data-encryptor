package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.SecretKey;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

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

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
