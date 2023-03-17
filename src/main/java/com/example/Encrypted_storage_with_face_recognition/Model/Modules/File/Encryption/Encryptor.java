package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Encryption;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Slf4j
@Service
public class Encryptor {

    @Autowired
    private KeyStore keyStore;
    public static Map<String, byte[]> encrypt(File file)  {
        try {
            byte[] plainText  = Files.readAllBytes(Path.of(file.getPath()));

            byte[] fileDigest = getFileDigest(plainText);

            Cipher cipher = getCipher();

            Map<String, byte[]> encryptionResult = new HashMap<>();

            encryptionResult.put("encryptedFile", cipher.doFinal(plainText));
            encryptionResult.put("fileDigest", fileDigest);

            return encryptionResult;

        } catch (IllegalBlockSizeException | IOException | BadPaddingException e) {
            throw new RuntimeException(e);
        }

    }

    private static byte[] getFileDigest(byte[] plainText) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(plainText);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static Cipher getCipher() {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            SecretKey secretKey = getSecretKey();

            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            return cipher;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }
    private static SecretKey getSecretKey() {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

            SecureRandom secureRandom = new SecureRandom();

            int keyBitSize = 256;

            keyGenerator.init(keyBitSize, secureRandom);

            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


    }
}
