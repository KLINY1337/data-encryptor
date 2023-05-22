package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Encryption;


import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.Cipher.CipherService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.Digest.DigestService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.KeyStore.KeyStoreService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.MetaData.FileMetaDataService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.SecretKey.SecretKeyService;
import gov.sandia.cognition.util.Triple;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.ByteArrayOutputStream;
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
    private final KeyStoreService keyStoreService;
    private final DigestService digestService;
    private final CipherService cipherService;
    private final SecretKeyService secretKeyService;
    private final FileMetaDataService fileMetaDataService;
    public Encryptor(KeyStoreService keyStoreService,
                     DigestService digestService,
                     CipherService cipherService,
                     SecretKeyService secretKeyService,
                     FileMetaDataService fileMetaDataService){
        this.keyStoreService = keyStoreService;
        this.digestService = digestService;
        this.cipherService = cipherService;
        this.secretKeyService = secretKeyService;
        this.fileMetaDataService = fileMetaDataService;
    }
    public Map<String, byte[]> encrypt(File file)  {
        try {
            byte[] fileBytes  = Files.readAllBytes(Path.of(file.getCanonicalPath()));
            byte[] fileMetaData = fileMetaDataService.getFileMetaData(file);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            byteArrayOutputStream.write(fileBytes);
            byteArrayOutputStream.write(fileMetaData);
            byteArrayOutputStream.write(fileMetaData.length);

            byte[] fileBytesWithMetadata = byteArrayOutputStream.toByteArray();

            byte[] fileBytesDigest = digestService.getDigest(fileBytesWithMetadata);

            SecretKey secretKey = secretKeyService.getSecretKey();
            byte[] encryptedBytes = getEncryptedBytes(fileBytesWithMetadata, secretKey);
            byte[] encryptedBytesDigest = digestService.getDigest(encryptedBytes);

            Triple<String, KeyStore.SecretKeyEntry, KeyStore.ProtectionParameter> entryParameters = keyStoreService.getParametersForStoringKey(
                    null,
                    encryptedBytes,
                    encryptedBytesDigest,
                    secretKey);

            KeyStore keyStore = keyStoreService.getKeyStore();
            keyStore.setEntry(entryParameters.getFirst().toLowerCase(), entryParameters.getSecond(), entryParameters.getThird());

            Map<String, byte[]> encryptionResult = new HashMap<>();
            encryptionResult.put("encryptedBytes", encryptedBytes);
            encryptionResult.put("fileBytesDigest", fileBytesDigest);
            encryptionResult.put("alias", entryParameters.getFirst().getBytes());

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
