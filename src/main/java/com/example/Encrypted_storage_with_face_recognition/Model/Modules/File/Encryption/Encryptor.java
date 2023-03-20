package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Encryption;


import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.Cipher.CipherService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.Digest.DigestService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.KeyStore.KeyStoreService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.Create.SecretKey.SecretKeyService;
import gov.sandia.cognition.util.DefaultTriple;
import gov.sandia.cognition.util.Triple;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Slf4j
@Service
public class Encryptor {
    private final KeyStoreService keyStoreService;
    private final DigestService digestService;
    private final CipherService cipherService;
    private final SecretKeyService secretKeyService;
    public Encryptor(KeyStoreService keyStoreService,
                     DigestService digestService,
                     CipherService cipherService,
                     SecretKeyService secretKeyService){
        this.keyStoreService = keyStoreService;
        this.digestService = digestService;
        this.cipherService = cipherService;
        this.secretKeyService = secretKeyService;
    }
    public Map<String, byte[]> encrypt(File file)  {
        try {
            byte[] fileBytes  = Files.readAllBytes(Path.of(file.getPath()));
            byte[] fileBytesDigest = digestService.getDigest(fileBytes);

            SecretKey secretKey = secretKeyService.getSecretKey();

            byte[] encryptedBytes = getEncryptedBytes(fileBytes, secretKey);
            byte[] encryptedBytesDigest = digestService.getDigest(encryptedBytes);

            Triple<String, KeyStore.SecretKeyEntry, KeyStore.ProtectionParameter> entryParameters = keyStoreService.getParametersForStoringKey(
                    encryptedBytes,
                    encryptedBytesDigest,
                    secretKey);

            KeyStore keyStore = keyStoreService.getKeyStore();
            keyStore.setEntry(entryParameters.getFirst(), entryParameters.getSecond(), entryParameters.getThird());

            Map<String, byte[]> encryptionResult = new HashMap<>();
            encryptionResult.put("encryptedBytes", encryptedBytes);
            encryptionResult.put("fileBytesDigest", fileBytesDigest);

            return encryptionResult;

        } catch (IllegalBlockSizeException | IOException | BadPaddingException | KeyStoreException e) {
            throw new RuntimeException(e);
        }

    }

    private byte[] getEncryptedBytes(byte[] fileBytes, SecretKey secretKey) throws IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = cipherService.getCipher(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(fileBytes);
    }

}
