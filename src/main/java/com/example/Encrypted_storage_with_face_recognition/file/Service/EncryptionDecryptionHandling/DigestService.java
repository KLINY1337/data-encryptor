package com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling;

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

    /**

     * Calculates the digest (hash) of the provided byte array using the specified hashing method.
     * @param plainText the byte array to calculate the digest for
     * @return the calculated digest as a byte array
     * @throws RuntimeException if the specified hashing method is not available
     */
    public byte[] getDigest(byte[] plainText) {

        try {

            MessageDigest messageDigest = MessageDigest.getInstance(hashingMethod);

            return messageDigest.digest(plainText);
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);
        }
    }
}
