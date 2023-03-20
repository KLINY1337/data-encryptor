package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.Digest;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Setter
@Getter
@Slf4j
@Service
@PropertySource("application.properties")
public class DigestService {

    private final String hashingMethod;

    public DigestService(@Value("${digest.hashing.method}") String hashingMethod) {
        this.hashingMethod = hashingMethod;
    }

    public byte[] getDigest(byte[] plainText) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(hashingMethod);
            return messageDigest.digest(plainText);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
