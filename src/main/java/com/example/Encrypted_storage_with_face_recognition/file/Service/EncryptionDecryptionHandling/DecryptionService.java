package com.example.Encrypted_storage_with_face_recognition.file.Service.EncryptionDecryptionHandling;

import com.example.Encrypted_storage_with_face_recognition.file.Service.FileHandling.FileMetaDataService;
import gov.sandia.cognition.util.Triple;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Slf4j
@Service
public class DecryptionService {

    private final KeyStoreService keyStoreService;
    private final DigestService digestService;
    private final CipherService cipherService;
    private final SecretKeyService secretKeyService;
    private final FileMetaDataService fileMetaDataService;
    public DecryptionService(KeyStoreService keyStoreService,
                             DigestService digestService,
                             CipherService cipherService,
                             SecretKeyService secretKeyService, FileMetaDataService fileMetaDataService){

        this.keyStoreService = keyStoreService;
        this.digestService = digestService;
        this.cipherService = cipherService;
        this.secretKeyService = secretKeyService;
        this.fileMetaDataService = fileMetaDataService;
    }

    /**

     * Decrypts a file and returns the decrypted data as a map of byte arrays.

     * @param file the file to be decrypted

     * @return a map containing the decrypted file data, including the decrypted bytes, decrypted bytes digest, and the file alias

     * @throws RuntimeException if an error occurs during the decryption process
     */
    public Map<String, byte[]> decrypt(File file){

        try {

            byte[] encryptedBytes  = Files.readAllBytes(Path.of(file.getPath()));
            byte[] encryptedBytesDigest = digestService.getDigest(encryptedBytes);

            KeyStore keyStore = keyStoreService.getKeyStore();

            Triple<String, KeyStore.SecretKeyEntry, KeyStore.ProtectionParameter> entryParameters = keyStoreService.getParametersForStoringKey(
                    file.getName().toLowerCase(),
                    encryptedBytes,
                    encryptedBytesDigest,
                    null);

            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(entryParameters.getFirst(), entryParameters.getThird());

            byte[] decryptedBytes = getDecryptedBytes(encryptedBytes, entry.getSecretKey());
            byte[] decryptedBytesDigest = digestService.getDigest(decryptedBytes);

            Map<String, byte[]> fileData = fileMetaDataService.getSeparatedFileData(decryptedBytes);

            Map<String, byte[]> decryptionResult = new HashMap<>();

            decryptionResult.put("decryptedBytes", fileData.get("fileBytes"));
            decryptionResult.put("decryptedBytesDigest", decryptedBytesDigest);
            decryptionResult.put("alias", fileData.get("fileMetaData"));

            return decryptionResult;

        } catch (IOException | UnrecoverableEntryException | NoSuchAlgorithmException | KeyStoreException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    /**

    * Decrypts the provided byte array using the specified secret key and returns the decrypted bytes.
    * @param fileBytes the byte array to be decrypted
    * @param secretKey the secret key used for decryption
    * @return the decrypted byte array
    * @throws IOException if an I/O error occurs
    * @throws IllegalBlockSizeException if the input data length is not a multiple of the block size or if padding is incorrectly applied
    * @throws BadPaddingException if the input data has been corrupted or if padding is incorrectly applied
     */
    private byte[] getDecryptedBytes(byte[] fileBytes, SecretKey secretKey) throws IOException, IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = cipherService.getCipher(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(fileBytes);
    }
}
