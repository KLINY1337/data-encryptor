package com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Decryption;

import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.Cipher.CipherService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.Digest.DigestService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.KeyStore.KeyStoreService;
import com.example.Encrypted_storage_with_face_recognition.Model.Modules.File.EncryptionDecryption.Create.SecretKey.SecretKeyService;
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
public class Decryptor {

    private final KeyStoreService keyStoreService;
    private final DigestService digestService;
    private final CipherService cipherService;
    private final SecretKeyService secretKeyService;
    public Decryptor(KeyStoreService keyStoreService,
                     DigestService digestService,
                     CipherService cipherService,
                     SecretKeyService secretKeyService){
        this.keyStoreService = keyStoreService;
        this.digestService = digestService;
        this.cipherService = cipherService;
        this.secretKeyService = secretKeyService;
    }

    public Map<String, byte[]> decrypt(File file){
        try {
            byte[] encryptedBytes  = Files.readAllBytes(Path.of(file.getPath()));
            byte[] encryptedBytesDigest = digestService.getDigest(encryptedBytes);

            KeyStore keyStore = keyStoreService.getKeyStore();

            Triple<String, KeyStore.SecretKeyEntry, KeyStore.ProtectionParameter> entryParameters = keyStoreService.getParametersForStoringKey(
                    encryptedBytes,
                    encryptedBytesDigest,
                    null);

            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(entryParameters.getFirst(), entryParameters.getThird());

            byte[] decryptedBytes = getDecryptedBytes(encryptedBytes, entry.getSecretKey());
            byte[] decryptedBytesDigest = digestService.getDigest(decryptedBytes);

            Map<String, byte[]> decryptionResult = new HashMap<>();
            decryptionResult.put("decryptedBytes", decryptedBytes);
            decryptionResult.put("decryptedBytesDigest", decryptedBytesDigest);

            return decryptionResult;

        } catch (IOException | UnrecoverableEntryException | NoSuchAlgorithmException | KeyStoreException |
                 IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getDecryptedBytes(byte[] fileBytes, SecretKey secretKey) throws IOException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = cipherService.getCipher(Cipher.DECRYPT_MODE, secretKey);

        return cipher.doFinal(fileBytes);
    }
}
