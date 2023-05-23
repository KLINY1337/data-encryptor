package com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling;


import com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.FileMetaDataService;
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
public class EncryptionService {
    private final KeyStoreService keyStoreService;
    private final DigestService digestService;
    private final CipherService cipherService;
    private final SecretKeyService secretKeyService;
    private final FileMetaDataService fileMetaDataService;
    public EncryptionService(KeyStoreService keyStoreService,
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

    /**

     * Encrypts the provided file and returns the encrypted data as a map of byte arrays.

     * @param file the file to be encrypted
     * @return a map containing the encrypted file data, including the encrypted bytes, file bytes digest, and the alias used for storing the encrypted data in the keystore
     * @throws RuntimeException if an error occurs during the encryption process
     */
    public Map<String, byte[]> encrypt(File file)  {

        try {

            byte[] fileBytes  = Files.readAllBytes(Path.of(file.getAbsolutePath()));
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

    /**

     * Encrypts the provided byte array using the specified secret key and returns the encrypted bytes.
     * @param fileBytes the byte array to be encrypted
     * @param secretKey the secret key used for encryption
     * @return the encrypted byte array
     * @throws IOException if an I/O error occurs
     * @throws IllegalBlockSizeException if the input data length is not a multiple of the block size or if padding is incorrectly applied
     * @throws BadPaddingException if the input data has been corrupted or if padding is incorrectly applied
     */
    private byte[] getEncryptedBytes(byte[] fileBytes, SecretKey secretKey) throws IOException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = cipherService.getCipher(Cipher.ENCRYPT_MODE, secretKey);

        return cipher.doFinal(fileBytes);
    }

}
